package com.dtdennis.currency.ui

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dtdennis.currency.core.conversion.CurrencyConverter
import com.dtdennis.currency.core.currencies.Currency
import com.dtdennis.currency.core.currencies.SupportedCurrenciesInteractor
import com.dtdennis.currency.core.rates.CurrencyRatesInteractor
import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import com.dtdennis.currency.core.rates.LocalCurrencyRatesInteractor
import com.dtdennis.currency.data.util.Logger
import com.dtdennis.currency.ui.util.LiveDataEvent
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "MainVM"
private const val DEFAULT_CURRENCY_CODE = "EUR"
private const val DEFAULT_CURRENCY_NAME = "Euro"
private const val DEFAULT_VALUE = 1.0

class MainVM @Inject constructor(
    private val logger: Logger,
    private val supportedCurrenciesInteractor: SupportedCurrenciesInteractor,
    private val currencyRatesInteractor: CurrencyRatesInteractor,
    private val localCurrencyRatesInteractor: LocalCurrencyRatesInteractor
) : ViewModel() {
    private val DEFAULT_BASELINE = UserBaseline(
        DEFAULT_CURRENCY_CODE,
        DEFAULT_CURRENCY_NAME,
        DEFAULT_VALUE,
        mapOf(
            DEFAULT_CURRENCY_CODE to 0
        )
    )

    private val currentBaselineSub = PublishSubject.create<UserBaseline>()
    private val currentBaselineObs = currentBaselineSub.startWith(DEFAULT_BASELINE)

    // Not super reactive/functional (probably a more sophisticated way via Rx),
    // But this is a quick and easy way to do an immediate recalculation when the baseline changes
    // (as opposed to waiting for the interactor/repository to return a new manifest)
    private var latestStreams: Triple<UserBaseline, CurrencyRatesManifest, Map<String, Currency>>? =
        null

    private val conversionListFlowable =
        Observable.interval(1L, TimeUnit.SECONDS, Schedulers.io())
            .withLatestFrom(
                currentBaselineObs,
                BiFunction { _: Long, baseline: UserBaseline ->
                    return@BiFunction baseline
                }
            )
            .flatMapSingle(::getRatesWithLocalConversion)
            .flatMap(::combineDataStreams)
            .map(::composeConversionList)
            .toFlowable(BackpressureStrategy.LATEST)
            .onErrorReturn {
                logger.d(TAG, "Unable to do any sort of conversions. Returning empty list")
                ConversionList(DEFAULT_BASELINE, emptyList())
            }

    val conversionList = LiveDataReactiveStreams.fromPublisher(conversionListFlowable)

    /**
     * Immediately return the re-converted item without waiting for a new manifest
     */
    fun onBaselineChanged(newUserBaseline: UserBaseline): ConversionList? {
        logger.d(TAG, "Baseline updated: $newUserBaseline")

        currentBaselineSub.onNext(newUserBaseline)

        if (latestStreams != null) {
            return composeConversionList(latestStreams!!)
        }

        return null
    }

    private fun getRatesWithLocalConversion(baseline: UserBaseline): Single<Pair<UserBaseline, CurrencyRatesManifest>> {
        return currencyRatesInteractor
            .getRates(baseline.code)
            .map { manifest ->
                Pair(baseline, manifest)
            }
            .onErrorReturn {
                handleGetRatesError(baseline, it)
            }
    }

    private fun handleGetRatesError(attemptedBaseline: UserBaseline, error: Throwable): Pair<UserBaseline, CurrencyRatesManifest> {
        logger.e(TAG, error)

        return if (latestStreams != null) {
            logger.d(TAG, "Doing local conversion")
            val newManifest =
                localCurrencyRatesInteractor
                    .rebaseCurrencyRatesManifest(
                        attemptedBaseline.code,
                        latestStreams!!.second
                    )

            Pair(attemptedBaseline, newManifest)
        } else throw error
    }

    private fun combineDataStreams(baselineAndManifest: Pair<UserBaseline, CurrencyRatesManifest>): Observable<Triple<UserBaseline, CurrencyRatesManifest, Map<String, Currency>>> {
        val (baseline, manifest) = baselineAndManifest
        return supportedCurrenciesInteractor
            .getSupportedCurrencies()
            .toObservable()
            .map { supportedCurrencies ->
                // Maintain state for quick conversion
                latestStreams =
                    Triple(baseline, manifest, supportedCurrencies.associateBy { it.code })
                return@map latestStreams
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