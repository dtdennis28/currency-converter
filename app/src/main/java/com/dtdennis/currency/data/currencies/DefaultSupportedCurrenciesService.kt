package com.dtdennis.currency.data.currencies

import android.content.res.AssetManager
import com.dtdennis.currency.core.currencies.Currency
import com.google.gson.Gson
import com.google.gson.JsonParser

/**
 * Can be replaced by a network service,
 * just fetch supported currencies from assets JSON file
 */
private const val FILENAME = "currency_codes_manifest.json"

class DefaultSupportedCurrenciesService(
    private val assetManager: AssetManager
) {
    private val parser = JsonParser()

    fun getSupportedCurrencies(): List<Currency> {
        var json: String? = null

        assetManager
            .open(FILENAME)
            .bufferedReader()
            .use {
                json = it.readText()
            }

        if (json != null) {
            return mapJsonToCurrencyList(json!!)
        } else {
            throw UnknownError("An unknown error occurred while fetching local currencies JSON")
        }
    }

    private fun mapJsonToCurrencyList(json: String): List<Currency> {
        val jsonObject = parser.parse(json).asJsonObject

        return jsonObject
            .keySet()
            .map {
                Currency(it, jsonObject.get(it).asString)
            }
    }
}