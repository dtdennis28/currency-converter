package com.dtdennis.currency.core.currencies

import io.reactivex.Single
import javax.inject.Inject

/**
 * Use case(s) for interacting with supported [Currency]s
 */
class SupportedCurrenciesInteractor @Inject constructor(private val repository: SupportedCurrenciesRepository) {
    fun getSupportedCurrencies(): Single<List<Currency>> = repository.getSupportedCurrencies()
}