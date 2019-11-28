package com.dtdennis.revolut.data.currencies

import com.dtdennis.revolut.core.currencies.Currency
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class SupportedCurrenciesRepositoryImplTest {
    private val fakeList = listOf(Currency("ABC", "Fake"))
    private val supportedCurrSvc = mock<SupportedCurrenciesLocalService> {
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