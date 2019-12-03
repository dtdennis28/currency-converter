package com.dtdennis.currency.data.rates.storage

import com.dtdennis.currency.util.mothers.CurrencyRatesManifestMother
import org.junit.Test

class MemCurrencyRatesStorageTest {
    private val storage = MemCurrencyRatesStorage()
    private val testManifest = CurrencyRatesManifestMother.defaultTestCurrencyRatesManifest()

    @Test
    fun Test_SetAndGetRates() {
        storage
            .setRates(testManifest)
            .test()
            .assertComplete()

        storage
            .getRates()
            .test()
            .assertValue(testManifest)
    }

    @Test
    fun Test_ClearRates() {
        storage
            .setRates(testManifest)
            .test()
            .assertComplete()


        storage
            .clearRates()
            .test()
            .assertComplete()
    }
}