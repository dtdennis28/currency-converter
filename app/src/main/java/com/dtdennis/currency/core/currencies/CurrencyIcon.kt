package com.dtdennis.currency.core.currencies

/**
 * An icon to associate to a [Currency];
 *
 * Can be of type [CurrencyIcon.Type.LOCAL] or [CurrencyIcon.Type.REMOTE]
 *
 * E.g. local would be a local resource drawable while remote a PNG URL
 */
data class CurrencyIcon(
    val type: Type,
    val location: String
) {
    enum class Type {
        LOCAL,
        REMOTE
    }
}