package com.dtdennis.currency.ui.entities

import com.dtdennis.currency.core.currencies.CurrencyIcon

data class CurrencyLineItem(
    val code: String,
    val name: String,
    val icon: CurrencyIcon,
    val value: Double
)