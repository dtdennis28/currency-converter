package com.dtdennis.currency.data.rates.storage

import android.content.SharedPreferences
import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Simple storage of a single rates manifest to JSON.
 *
 * Backed by a [SharedPreferences], uses [Gson] for (de)serialization
 */

private const val MANIFEST_PREFS_KEY = "com.dtdennis.currency.RatesManifest"

class PrefsCurrencyRatesStorage
@Inject constructor(
    private val sharedPreferences: SharedPreferences
) : CurrencyRatesStorage {
    private val gson = Gson()

    override fun setRates(currencyRatesManifest: CurrencyRatesManifest): Completable =
        Completable.fromAction {
            sharedPreferences
                .edit()
                .putString(MANIFEST_PREFS_KEY, gson.toJson(currencyRatesManifest))
                .commit()
        }

    override fun getRates(): Maybe<CurrencyRatesManifest> =
        Maybe.fromCallable {
            sharedPreferences
                .getString(MANIFEST_PREFS_KEY, null)
                ?.let {
                    gson.fromJson(it, CurrencyRatesManifest::class.java)
                }
        }

    override fun clearRates(): Completable =
        Completable.fromAction {
            sharedPreferences
                .edit()
                .remove(MANIFEST_PREFS_KEY)
                .commit()
        }
}