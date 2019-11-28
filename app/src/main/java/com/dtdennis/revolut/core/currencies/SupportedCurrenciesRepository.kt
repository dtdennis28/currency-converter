package com.dtdennis.revolut.core.currencies

import io.reactivex.Single

/**
 * Represents a repository backed by a data source, which provides access to the list of
 * supported currencies.
 */
interface SupportedCurrenciesRepository {
    fun getSupportedCurrencies(): Single<List<Currency>>
}