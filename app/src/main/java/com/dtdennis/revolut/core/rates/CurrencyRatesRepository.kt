package com.dtdennis.revolut.core.rates

import io.reactivex.Flowable

/**
 * Represents a repository of currency rates with any given data source
 * (or combination of data sources) as its backing
 */
interface CurrencyRatesRepository {
    /**
     * Stream live currency rates, should update every 1 second
     */
    fun streamRates(baseCurrency: String): Flowable<CurrencyRatesManifest>
}