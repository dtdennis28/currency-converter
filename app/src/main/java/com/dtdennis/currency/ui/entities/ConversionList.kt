package com.dtdennis.currency.ui.entities

/**
 * Represents a list / recycler view of data
 */
data class ConversionList(
    val baseline: UserBaseline,
    val lineItems: List<CurrencyLineItem>
)