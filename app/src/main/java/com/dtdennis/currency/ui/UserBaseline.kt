package com.dtdennis.currency.ui

data class UserBaseline(
    val code: String,
    val value: Double,
    val positions: Map<String, Int>
)