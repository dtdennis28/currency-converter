package com.dtdennis.currency.data.currencies

import android.content.res.AssetManager
import com.dtdennis.currency.core.currencies.Currency
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

/**
 * Just reads the bundled JSON file for a list of supported currencies.
 * Can be API-backed in the future
 */
private const val FILENAME = "currency_codes_manifest.json"

class DefaultSupportedCurrenciesService(
    private val assetManager: AssetManager
) {
    private val gson = Gson()

    fun read(): List<Currency> {
        var json: String? = null

        assetManager
            .open(FILENAME)
            .bufferedReader()
            .use {
                json = it.readText()
            }

        if (json != null) {
            return gson.fromJson(json, object : TypeToken<List<Currency>>(){}.type)
        } else {
            throw UnknownError("An unknown error occurred while fetching local currencies JSON")
        }
    }
}