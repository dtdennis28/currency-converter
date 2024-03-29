package com.dtdennis.currency.ui.entities

import com.dtdennis.currency.core.user.UserBaseline

/**
 * Represents a list / recycler view of data
 */
data class ConversionList(
    val baseline: UserBaseline,
    val lineItems: List<CurrencyLineItem>
)