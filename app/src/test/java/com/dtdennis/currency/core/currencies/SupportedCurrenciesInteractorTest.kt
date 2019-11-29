package com.dtdennis.currency.core.currencies

import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class SupportedCurrenciesInteractorTest {
    private val testList = listOf(
        Currency("EUR", "Euro"),
        Currency("USD", "US Dollar"),
        Currency("GBP", "British Pound")
    )

    private val testRepo = DummyRepository(testList)

    private lateinit var interactor: SupportedCurrenciesInteractor

    @Before
    fun setUp() {
        // Default to a valid list
        testRepo.setList(testList)
        interactor = SupportedCurrenciesInteractor(testRepo)
    }

    @Test
    fun ShouldNot_ManipulateList_When_GetSupportedCurrencies() {
        // Given an original list of supported currencies
        // When get supported currencies
        // Then original data is not manipulated
        interactor
            .getSupportedCurrencies()
            .test()
            .assertValue(testList)
    }

    @Test
    fun Should_PropagateError_When_ErrorThrown() {
        // Given no list of currencies / an error will return
        testRepo.setList(null)

        // When get supported currencies
        // Then an error is propagated
        interactor
            .getSupportedCurrencies()
            .test()
            .assertError(Throwable::class.java)
    }

    class DummyRepository(private var currencyList: List<Currency>? = null) :
        SupportedCurrenciesRepository {
        override fun getSupportedCurrencies(): Single<List<Currency>> {
            return Single.create { emitter ->
                if (currencyList != null) {
                    emitter.onSuccess(currencyList!!)
                } else {
                    emitter.onError(Exception("Test exception"))
                }
            }
        }

        fun setList(currencyList: List<Currency>? = null) {
            this.currencyList = currencyList
        }
    }
}