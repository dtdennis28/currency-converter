package com.dtdennis.currency.core.rates

import io.reactivex.Observable

/**
 * Represents a repository of currency rates with any given data source
 * (or combination of data sources) as its backing
 */
interface CurrencyRatesRepository {
    fun streamRates(): Observable<CurrencyRatesManifest>
}