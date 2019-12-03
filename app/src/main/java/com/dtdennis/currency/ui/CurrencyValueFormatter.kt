package com.dtdennis.currency.ui

object CurrencyValueFormatter {
    fun format(value: Double): String {
        return String.format("%.3f", value)
    }
}