package com.dtdennis.revolut.core.currencies

import io.reactivex.Single

/**
 * Use case(s) for interacting with supported [Currency]s
 */
class SupportedCurrenciesInteractor(private val repository: SupportedCurrenciesRepository) {
    fun getSupportedCurrencies(): Single<List<Currency>> = repository.getSupportedCurrencies()
}