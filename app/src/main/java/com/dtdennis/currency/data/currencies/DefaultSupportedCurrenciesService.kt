package com.dtdennis.currency.data.currencies

import android.content.res.AssetManager
import com.dtdennis.currency.core.currencies.Currency
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

/**
 * Can be replaced by a network service,
 * just fetch supported currencies from assets JSON file
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