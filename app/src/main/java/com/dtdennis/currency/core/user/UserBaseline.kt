package com.dtdennis.currency.core.user

/**
 * Represents the current user "selection" (which currency is at the top, its input value, and the
 * resulting positions of the other currencies.
 *
 * Positions are stored so that the user-defined order (e.g. user taps on one currency, it floats
 * to top, and now the list has a different order than what might originally come back from the VM).
 *
 * The VM can use these positions to maintain the order upon arrival of new currency rates.
 */
data class UserBaseline(
    val code: String,
    val name: String,
    val value: Double,
    val positions: Map<String, Int>
)