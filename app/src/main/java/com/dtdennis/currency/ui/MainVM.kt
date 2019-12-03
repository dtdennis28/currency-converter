package com.dtdennis.currency.ui

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.dtdennis.currency.core.conversion.CurrencyConverter
import com.dtdennis.currency.core.currencies.Currency
import com.dtdennis.currency.core.currencies.CurrencyIcon
import com.dtdennis.currency.core.currencies.SupportedCurrenciesInteractor
import com.dtdennis.currency.core.rates.CurrencyRatesInteractor
import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import com.dtdennis.currency.data.util.Logger
import com.dtdennis.currency.ui.entities.ConversionList
import com.dtdennis.currency.ui.entities.CurrencyLineItem
import com.dtdennis.currency.ui.entities.UserBaseline
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

private const val TAG = "MainVM"
private const val DEFAULT_CURRENCY_CODE = "EUR"
private const val DEFAULT_CURRENCY_NAME = "Euro"
private const val DEFAULT_VALUE = 1.0
private val DEFAULT_ICON = CurrencyIcon(
    CurrencyIcon.Type.LOCAL,
    "R.drawable.ic_currency_generic"
)

class MainVM @Inject constructor(
    private val logger: Logger,
    private val supportedCurrenciesInteractor: SupportedCurrenciesInteractor,
    private val currencyRatesInteractor: CurrencyRatesInteractor
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

    /**
     * Secondly stream,
     * combine with latest baseline -> bifunction to convert baseline from latest manifest
     */
    private val conversionListFlowable =
        currencyRatesInteractor
            .streamRates()
            .withLatestFrom(
                currentBaselineObs,
                BiFunction { manifest: CurrencyRatesManifest, baseline: UserBaseline ->
                    return@BiFunction Pair(baseline, manifest)
                }
            )
            .flatMap(::combineDataStreams)
            .map(::composeConversionList)
            .onErrorReturn {
                logger.d(TAG, "Unable to do any sort of conversions. Returning empty list")
                ConversionList(DEFAULT_BASELINE, emptyList())
            }
            .toFlowable(BackpressureStrategy.LATEST)

    val conversionList = LiveDataReactiveStreams.fromPublisher(conversionListFlowable)

    /**
     * Immediately return the re-converted item without waiting for a new manifest
     */
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
        val baselineCurrency = getCurrency(baseline.code, supportedCurrencies)
        newList.add(
            CurrencyLineItem(
                baseline.code,
                baselineCurrency.name,
                baselineCurrency.icon,
                baseline.value
            )
        )

        // Subsequent already-ordered items
        baseline.positions.forEach { entry ->
            if (manifest.rates.containsKey(entry.key) && entry.key != baseline.code) {
                newList.add(
                    mapToCurrencyLineItem(
                        baseline.code,
                        baseline.value,
                        entry.key,
                        converter,
                        supportedCurrencies
                    )
                )
            }
        }

        // Any additional currencies not previously in list
        val newItemsMap = newList.associateBy { it.code }
        manifest.rates.forEach { rate ->
            if (!newItemsMap.containsKey(rate.key) && rate.key != baseline.code) {
                newList.add(
                    mapToCurrencyLineItem(
                        baseline.code,
                        baseline.value,
                        rate.key,
                        converter,
                        supportedCurrencies
                    )
                )
            }
        }

        return ConversionList(baseline, newList)
    }

    private fun getCurrency(code: String, supportedCurrencies: Map<String, Currency>): Currency {
        // Check local list of supported currencies for a user-readable name
        return supportedCurrencies[code] ?: Currency(code, code, DEFAULT_ICON)
    }

    private fun mapToCurrencyLineItem(
        baseCode: String,
        baseValue: Double,
        toCode: String,
        converter: CurrencyConverter,
        supportedCurrencies: Map<String, Currency>
    ): CurrencyLineItem {
        val convertedValue = converter.convert(baseValue, baseCode, toCode)

        val currency = getCurrency(toCode, supportedCurrencies)

        return CurrencyLineItem(
            toCode,
            currency.name,
            currency.icon,
            convertedValue
        )
    }
}