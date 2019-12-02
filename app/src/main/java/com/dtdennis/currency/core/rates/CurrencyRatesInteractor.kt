package com.dtdennis.currency.core.rates

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.reactivestreams.Publisher
import javax.inject.Inject

/**
 * Use case(s) for fetching the rates from a data source
 */
class CurrencyRatesInteractor @Inject constructor(private val repository: CurrencyRatesRepository) {
    fun streamRates(baseCurrency: String): Flowable<CurrencyRatesManifest> =
        repository.streamRates(baseCurrency)

    fun getRates(baseCurrency: String): Single<CurrencyRatesManifest> =
        repository.getRates(baseCurrency)

    fun streamRates(baseCurrencyPublisher: Observable<String>): Flowable<CurrencyRatesManifest> =
        repository.streamRates(baseCurrencyPublisher)
}