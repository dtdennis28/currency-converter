package com.dtdennis.currency.core.rates

import io.reactivex.Observable
import javax.inject.Inject

/**
 * Use case(s) for fetching the rates from a data source
 */
class CurrencyRatesInteractor @Inject constructor(private val repository: CurrencyRatesRepository) {
    fun streamRates(): Observable<CurrencyRatesManifest> = repository.streamRates()
}