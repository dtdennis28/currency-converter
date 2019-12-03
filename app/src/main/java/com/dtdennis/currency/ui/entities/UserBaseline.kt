package com.dtdennis.currency.ui.entities

data class UserBaseline(
    val code: String,
    val name: String,
    val value: Double,
    val positions: Map<String, Int>
)