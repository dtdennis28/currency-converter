package com.dtdennis.currency.data.rates

import android.content.res.AssetManager
import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import com.google.gson.Gson
import javax.inject.Inject

private const val FILENAME = "default_rates_manifest.json"
class DefaultCurrencyRatesService @Inject constructor(
    private val assetManager: AssetManager
) {
    private val gson = Gson()

    fun read(): CurrencyRatesManifest {
        var json: String? = null

        assetManager
            .open(FILENAME)
            .bufferedReader()
            .use {
                json = it.readText()
            }

        if (json != null) {
            return gson.fromJson(json, CurrencyRatesManifest::class.java)
        } else {
            throw UnknownError("An unknown error occurred while fetching local currencies JSON")
        }
    }
}