package com.dtdennis.currency.core.currencies

import io.reactivex.Single
import javax.inject.Inject

/**
 * Use case for fetching the list of supported currencies
 *
 * Supported in this context means we have a name and icon to associate to the currency code.
 *
 * The API could return a code for which we do not have these, so it would be "unsupported"
 * (though still entirely usable in the list of currencies to use for conversion)
 */
class SupportedCurrenciesInteractor @Inject constructor(private val repository: SupportedCurrenciesRepository) {
    fun getSupportedCurrencies(): Single<List<Currency>> = repository.getSupportedCurrencies()
}