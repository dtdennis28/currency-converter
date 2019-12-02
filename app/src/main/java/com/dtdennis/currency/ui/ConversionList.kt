package com.dtdennis.currency.ui

data class ConversionList(
    val baseline: UserBaseline,
    val lineItems: List<CurrencyLineItem>
)