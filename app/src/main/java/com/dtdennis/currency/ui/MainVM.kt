package com.dtdennis.currency.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainVM @Inject constructor() : ViewModel() {
    private val _convertedCurrencies = MutableLiveData<List<ConvertedCurrency>>()
    val convertedCurrencies: LiveData<List<ConvertedCurrency>> = _convertedCurrencies

    init {
        _convertedCurrencies.value = listOf(
            ConvertedCurrency("ABC", "Abc", 1.00),
            ConvertedCurrency("DEF", "Def", 2.00),
            ConvertedCurrency("GHI", "Ghi", 3.00),
            ConvertedCurrency("JKL", "Jkl", 4.00),
            ConvertedCurrency("MNO", "Mno", 5.00)
        )
    }
}