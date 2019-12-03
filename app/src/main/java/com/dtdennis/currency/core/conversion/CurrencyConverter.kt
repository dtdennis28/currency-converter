package com.dtdennis.currency.core.conversion

/**
 * Given a map of rates keyed by the currency code (e.g. AUD),
 * convert a given value from one currency to the desired currency.
 */
class CurrencyConverter(private val ratesMap: Map<String, Double>) {
    fun convert(fromValue: Double, fromCode: String, toCode: String): Double {
        val fromRate = getRateByCode(fromCode)
        val toRate = getRateByCode(toCode)

        /*
            (Fv / Fr) = (Tv / Tr)
            =>
            Tv = (Fv / Fr) * Tr
         */
        return (fromValue / fromRate) * toRate
    }

    private fun getRateByCode(codeToRequire: String): Double {
        require(ratesMap.containsKey(codeToRequire)) { "Unexpected code: $codeToRequire. Please provide one of the following: ${ratesMap.keys}" }

        // Ignore compiler warning; we just required the map key above
        return ratesMap[codeToRequire]!!
    }
}