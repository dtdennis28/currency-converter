package com.dtdennis.currency.data.currencies

import com.dtdennis.currency.core.currencies.Currency
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class SupportedCurrenciesRepositoryImplTest {
    private val fakeList = listOf(Currency("ABC", "Fake"))
    private val supportedCurrSvc = mock<DefaultSupportedCurrenciesService> {
        on { getSupportedCurrencies() } doReturn fakeList
    }

    @Test
    fun SanityTest() {
        val repo = SupportedCurrenciesRepositoryImpl(supportedCurrSvc)

        repo
            .getSupportedCurrencies()
            .test()
            .assertValue(fakeList)
    }
}