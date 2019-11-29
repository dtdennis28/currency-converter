package com.dtdennis.currency.util.mothers

import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import java.util.*

object CurrencyRatesManifestMother {
    val DEFAULT_BASE_CURRENCY = "EUR"
    val DEFAULT_CURRENCY_RATES_DATE = Date().toString()
    val DEFAULT_CURRENCY_RATES_MAP = mapOf(
        "AUD" to 1.6122,
        "BGN" to 1.9507,
        "CAD" to 1.5298,
        "USD" to 1.1594,
        "GBP" to 0.90144
    )

    fun defaultTestCurrencyRatesManifest(): CurrencyRatesManifest = CurrencyRatesManifest(DEFAULT_BASE_CURRENCY, DEFAULT_CURRENCY_RATES_DATE, DEFAULT_CURRENCY_RATES_MAP)
}