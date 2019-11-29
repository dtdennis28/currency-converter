package com.dtdennis.currency.core.conversion

/**
 * Given a map of rates keyed by the currency code (e.g. AUD),
 * convert a given value from one currency to the desired currency.
 *
 * Note: the base currency is implied by the caller; meaning,
 * this class will simply take the value given, and multiply it by the
 * rate associated to the destination currency. There is no need to
 * describe or account for the original currency.
 */
class CurrencyConverter(private val ratesMap: Map<String, Double>) {
    fun convert(value: Double, destinationCode: String): Double =
        value * getRateByCode(destinationCode)

    private fun getRateByCode(codeToRequire: String): Double {
        require(ratesMap.containsKey(codeToRequire)) { "Unexpected code: $codeToRequire. Please provide one of the following: ${ratesMap.keys}" }

        // Ignore compiler warning; we just required the map key above
        return ratesMap[codeToRequire]!!
    }
}