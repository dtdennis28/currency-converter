package com.dtdennis.currency.ui

import com.dtdennis.currency.core.currencies.CurrencyIcon

data class CurrencyLineItem(
    val code: String,
    val name: String,
    val icon: CurrencyIcon,
    val value: Double
)