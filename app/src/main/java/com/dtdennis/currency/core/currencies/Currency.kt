package com.dtdennis.currency.core.currencies

/**
 * A currency identified by its code, and having a name and icon
 */
data class Currency(
    val code: String,
    val name: String,
    val icon: CurrencyIcon
)