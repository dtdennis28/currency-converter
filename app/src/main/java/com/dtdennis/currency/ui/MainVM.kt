package com.dtdennis.currency.ui

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.dtdennis.currency.core.conversion.CurrencyConverter
import com.dtdennis.currency.core.currencies.Currency
import com.dtdennis.currency.core.currencies.SupportedCurrenciesInteractor
import com.dtdennis.currency.core.rates.CurrencyRatesInteractor
import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import com.dtdennis.currency.data.util.Logger
import com.dtdennis.currency.ui.rx.UserChangesFlowableWrapper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "MainVM"
private const val DEFAULT_CURRENCY_CODE = "EUR"
private const val DEFAULT_VALUE = 1.0

class MainVM @Inject constructor(
    private val logger: Logger,
    private val supportedCurrenciesInteractor: SupportedCurrenciesInteractor,
    private val currencyRatesInteractor: CurrencyRatesInteractor
) : ViewModel() {
    private val DEFAULT_BASELINE = UserBaseline(
        DEFAULT_CURRENCY_CODE, DEFAULT_VALUE, mapOf(
            DEFAULT_CURRENCY_CODE to 0
        )
    )

    private val currentBaselineSub = PublishSubject.create<UserBaseline>()
    private val currentBaselineObs = currentBaselineSub.startWith(DEFAULT_BASELINE)

    private val conversionListFlowable =
        Observable.interval(1L, TimeUnit.SECONDS, Schedulers.io())
            .withLatestFrom(
                currentBaselineObs,
                BiFunction { _: Long, baseline: UserBaseline ->
                    return@BiFunction baseline
                }
            )
            .flatMapSingle { baseline ->
                currencyRatesInteractor
                    .getRates(baseline.code)
                    .map { manifest ->
                        Pair(baseline, manifest)
                    }
            }
            .flatMap(::combineDataStreams)
            .map(::composeConversionList)
            .toFlowable(BackpressureStrategy.LATEST)
            .doOnNext {
                logger.d(TAG, "onNext: $it")
            }

    val conversionList = LiveDataReactiveStreams.fromPublisher(conversionListFlowable)

    fun onBaselineChanged(newUserBaseline: UserBaseline) {
        logger.d(TAG, "Baseline updated: $newUserBaseline")
        currentBaselineSub.onNext(newUserBaseline)
    }

    private fun combineDataStreams(baselineAndManifest: Pair<UserBaseline, CurrencyRatesManifest>): Observable<Triple<UserBaseline, CurrencyRatesManifest, Map<String, Currency>>> {
        val (baseline, manifest) = baselineAndManifest
        return supportedCurrenciesInteractor
            .getSupportedCurrencies()
            .toObservable()
            .map { supportedCurrencies ->
                Triple(baseline, manifest, supportedCurrencies.associateBy { it.code })
            }
    }

    private fun composeConversionList(parts: Triple<UserBaseline, CurrencyRatesManifest, Map<String, Currency>>): ConversionList {
        val (baseline, manifest, supportedCurrencies) = parts
        val newList = mutableListOf<CurrencyLineItem>()
        val converter = CurrencyConverter(manifest.rates)

        // Initial line item (baseline)
        newList.add(
            CurrencyLineItem(
                baseline.code,
                getCurrencyName(baseline.code, supportedCurrencies),
                baseline.value
            )
        )

        // Subsequent already-ordered items
        baseline.positions.forEach { entry ->
            if (manifest.rates.containsKey(entry.key)) {
                newList.add(
                    mapToCurrencyLineItem(
                        entry.key,
                        baseline.value,
                        converter,
                        supportedCurrencies
                    )
                )
            }
        }

        // Any additional currencies not previously in list
        val newItemsMap = newList.associateBy { it.code }
        manifest.rates.forEach { rate ->
            if (!newItemsMap.containsKey(rate.key)) {
                newList.add(
                    mapToCurrencyLineItem(
                        rate.key,
                        baseline.value,
                        converter,
                        supportedCurrencies
                    )
                )
            }
        }

        return ConversionList(baseline, newList)
    }

    private fun getCurrencyName(code: String, supportedCurrencies: Map<String, Currency>): String {
        // Check local list of supported currencies for a user-readable name
        return supportedCurrencies[code]?.name ?: ""
    }

    private fun mapToCurrencyLineItem(
        code: String,
        baseValue: Double,
        converter: CurrencyConverter,
        supportedCurrencies: Map<String, Currency>
    ): CurrencyLineItem {
        val convertedValue = converter.convert(baseValue, code)

        val name = supportedCurrencies[code]?.name ?: ""

        return CurrencyLineItem(code, name, convertedValue)
    }
}