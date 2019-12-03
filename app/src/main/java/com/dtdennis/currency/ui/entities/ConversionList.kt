package com.dtdennis.currency.ui.entities

data class ConversionList(
    val baseline: UserBaseline,
    val lineItems: List<CurrencyLineItem>
)