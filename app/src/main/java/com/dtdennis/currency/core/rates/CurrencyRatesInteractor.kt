package com.dtdennis.currency.core.rates

import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Use case(s) for fetching the rates from a data source
 */
class CurrencyRatesInteractor @Inject constructor(private val repository: CurrencyRatesRepository) {
    fun streamRates(baseCurrency: String): Flowable<CurrencyRatesManifest> =
        repository.streamRates(baseCurrency)
}