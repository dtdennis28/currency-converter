package com.dtdennis.currency.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dtdennis.currency.core.currencies.CurrencyIcon
import com.dtdennis.currency.core.currencies.SupportedCurrenciesInteractor
import com.dtdennis.currency.core.rates.CurrencyRatesInteractor
import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import com.dtdennis.currency.util.TestLogger
import com.dtdennis.currency.util.mothers.DefaultServiceMothers.supportedCurrenciesRepository
import com.dtdennis.currency.util.observeOnce
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test

private const val EXPECTED_DEFAULT_CURRENCY_CODE = "EUR"
private const val EXPECTED_DEFAULT_CURRENCY_NAME = "Euro"
private const val EXPECTED_DEFAULT_VALUE = 1.0
private val EXPECTED_DEFAULT_ICON = CurrencyIcon(
    CurrencyIcon.Type.LOCAL,
    "ic_currency_generic"
)

class MainVMTest {
    // For Android Main Thread stuff related to Lifecycle/LiveData
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val supportedCurrenciesInteractor = SupportedCurrenciesInteractor(
        supportedCurrenciesRepository()
    )

    private lateinit var testRatesEmitter: ObservableEmitter<CurrencyRatesManifest>
    private val testRatesStream = Observable.create<CurrencyRatesManifest> {
        testRatesEmitter = it
    }
    private val currencyRatesInteractor = mock<CurrencyRatesInteractor> {
        on { streamRates() } doReturn testRatesStream
    }

    private val vm = MainVM(
        TestLogger(),
        supportedCurrenciesInteractor,
        mock(),
        currencyRatesInteractor
    )

    // Upon first observe, should emit a conversion list
    // with the default baseline
    @Test
    fun Should_EmitDefaultBaseline_When_FirstUse() {
        vm.conversionList.observeOnce {
//            assertThatt(it.baseline, equalTo())
        }
    }
    // Upon observe, should emit a CL
    // with the currency line items matching true conversions

    // Upon observe, should emit a CL
    // with the CLIs matching supported currency names & icons

    // Upon observe, should emit a CL
    // that matches the previous positions of the currencies
    // Baseline positions should be reflected in CL lineup

    // Upon observe, and then baseline changed
    // Should emit a new CL reflecting the new BL

    // Upon observe, then baseline changed, then destruction
    // And then re-observe
    // Should emit a CL reflecting the persisted BL

    // FUTURE
    // Upon observe, should emit a CL
    // that matches previous positions,
    // and adds new currencies at bottom
}