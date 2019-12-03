package com.dtdennis.currency.core.rates

import io.reactivex.Observable
import javax.inject.Inject

/**
 * Use case for streaming a periodically updating [CurrencyRatesManifest]
 */
class CurrencyRatesInteractor @Inject constructor(private val repository: CurrencyRatesRepository) {
    fun streamRates(): Observable<CurrencyRatesManifest> = repository.streamRates()
}