package com.dtdennis.currency.core.rates

import io.reactivex.Flowable

/**
 * Use case(s) for fetching the rates from a data source
 */
class CurrencyRatesInteractor(private val repository: CurrencyRatesRepository) {
    fun streamRates(baseCurrency: String): Flowable<CurrencyRatesManifest> =
        repository.streamRates(baseCurrency)
}