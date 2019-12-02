package com.dtdennis.currency.core.currencies

data class CurrencyIcon(
    val type: Type,
    val location: String
) {
    enum class Type {
        LOCAL,
        REMOTE
    }
}