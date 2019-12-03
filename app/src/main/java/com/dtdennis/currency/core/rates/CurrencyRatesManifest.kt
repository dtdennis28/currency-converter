package com.dtdennis.currency.core.rates

/**
 * Represents a manifest for all currencies we support for conversion
 */
data class CurrencyRatesManifest(
    /**
     * Currency being used as a base for the rate multiplier
     */
    val base: String,

    /**
     * Timestamp of when these rates were determined
     */
    val date: String,

    /**
     * Map of currency codes (e.g. EUR, USD, etc.) associated to their rate relative to the base currency.
     *
     * For example, if the base is EUR, the rates map might contain an entry "USD":1.1594
     *
     * This is so currencies can be converted in relation to each other
     */
    val rates: Map<String, Double>
)