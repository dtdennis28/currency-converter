package com.dtdennis.currency.core.rates

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.reactivestreams.Publisher

/**
 * Represents a repository of currency rates with any given data source
 * (or combination of data sources) as its backing
 */
interface CurrencyRatesRepository {
    /**
     * Stream live currency rates, should update every 1 second
     */
    @Deprecated("Use Single")
    fun streamRates(baseCurrency: String): Flowable<CurrencyRatesManifest>

    @Deprecated("Use other stream")
    fun getRates(baseCurrency: String): Single<CurrencyRatesManifest>

    fun streamRates(basePublisher: Observable<String>): Flowable<CurrencyRatesManifest>
}