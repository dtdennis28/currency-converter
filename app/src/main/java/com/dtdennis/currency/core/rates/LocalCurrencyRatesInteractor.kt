package com.dtdennis.currency.core.rates

import javax.inject.Inject

/**
 * Use cases pertaining to using only a locally-available manifest as a last resort.
 *
 * E.g. we can "reverse engineer" a manifest based on another currency to continue providing conversion
 */
class LocalCurrencyRatesInteractor @Inject constructor() {
    fun rebaseCurrencyRatesManifest(
        newBase: String,
        manifest: CurrencyRatesManifest
    ): CurrencyRatesManifest {
        val oldBase = manifest.base
        val oldConversionRate = manifest.rates[newBase]!!

        // Now "swap"
        val newConversionRate = 1 / oldConversionRate

        val newRates = manifest.rates.toMutableMap()
        newRates.remove(newBase)

        newRates[oldBase] = newConversionRate

        return manifest.copy(
            base = newBase,
            date = manifest.date,
            rates = newRates
        )
    }
}