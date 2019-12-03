package com.dtdennis.currency.data.rates.storage

import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import io.reactivex.Completable
import io.reactivex.Maybe

/**
 * Interface representing a data source / storage mechanism for a rates manifest.
 *
 * This can be fulfilled by any number of persistence options... database, SharedPrefs,
 * memory, flat file, etc.
 */
interface CurrencyRatesStorage {
    fun setRates(currencyRatesManifest: CurrencyRatesManifest): Completable
    fun getRates(): Maybe<CurrencyRatesManifest>
    fun clearRates(): Completable
}