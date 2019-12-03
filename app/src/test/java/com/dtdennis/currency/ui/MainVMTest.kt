package com.dtdennis.currency.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dtdennis.currency.core.conversion.CurrencyConverter
import com.dtdennis.currency.core.currencies.Currency
import com.dtdennis.currency.core.currencies.SupportedCurrenciesInteractor
import com.dtdennis.currency.core.rates.CurrencyRatesInteractor
import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import com.dtdennis.currency.core.user.UserBaselineInteractor
import com.dtdennis.currency.data.currencies.SupportedCurrenciesRepositoryImpl
import com.dtdennis.currency.data.rates.CurrencyRatesRepositoryImpl
import com.dtdennis.currency.data.rates.CurrencyRatesService
import com.dtdennis.currency.data.rates.storage.MemCurrencyRatesStorage
import com.dtdennis.currency.data.rates.storage.PrefsCurrencyRatesStorage
import com.dtdennis.currency.data.user.UserBaselineRepositoryImpl
import com.dtdennis.currency.data.user.UserBaselineStorage
import com.dtdennis.currency.util.TestLogger
import com.dtdennis.currency.util.TestSchedulerProvider
import com.dtdennis.currency.util.TestSharedPrefs
import com.dtdennis.currency.util.mothers.DefaultServiceMothers.defaultCurrencyRatesService
import com.dtdennis.currency.util.mothers.DefaultServiceMothers.defaultSupportedCurrencyService
import com.dtdennis.currency.util.observeOnce
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class MainVMTest {
    // For Android Main Thread stuff related to Lifecycle/LiveData
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val defaultSupportedCurrenciesService = defaultSupportedCurrencyService()
    private val supportedCurrenciesInteractor = SupportedCurrenciesInteractor(
        SupportedCurrenciesRepositoryImpl(defaultSupportedCurrenciesService)
    )

    private val userBaselineInteractor = UserBaselineInteractor(
        UserBaselineRepositoryImpl(UserBaselineStorage(TestSharedPrefs()))
    )

    private val ratesDataSourceHarness = TestRatesDataSourceHarness()

    private val currencyRatesInteractor = CurrencyRatesInteractor(
        ratesDataSourceHarness.repository
    )

    private val vm = MainVM(
        TestLogger(),
        supportedCurrenciesInteractor,
        userBaselineInteractor,
        ratesDataSourceHarness.defaultRatesService,
        currencyRatesInteractor
    )

    // Upon first observe, should emit a conversion list
    // with the default baseline
    @Test
    fun Should_EmitDefaultBaseline_When_FirstUse() {
        vm.conversionList.observeOnce {
            assertThat(it.baseline, equalTo(UserBaselineInteractor.DEFAULT_BASELINE))
        }
    }

    // Upon observe, should emit a CL
    // with the currency line items matching true conversions
    @Test
    fun Should_EmitTrulyConvertedCurrencies_When_RatesAvailable() {
        // Given default rates will be available on first use
        // with a default baseline
        val baseline = UserBaselineInteractor.DEFAULT_BASELINE
        val conversions = convertValueFromDefaultRates(baseline.code, baseline.value)

        // When observe
        // Get a ConversionList with accurate currency line items
        vm.conversionList.observeOnce {
            it.lineItems.forEach {
                assertThat(it.value, equalTo(conversions[it.code]))
            }
        }
    }

    // Upon observe, should emit a CL
    // with the CLIs matching supported currency names & icons
    @Test
    fun Should_EmitMatchingCurrencyMetadata_When_HasListOfSupportedCurrencies() {
        // Given default supported currencies will be available
        val currencies = defaultSupportedCurrenciesService.read()

        // When observe
        // Get CurrencyLineItems with correct currency metadata
        vm.conversionList.observeOnce {
            it.lineItems.forEach {
                val currencyFrom = Currency(it.code, it.name, it.icon)
                assertThat(currencies.contains(currencyFrom), equalTo(true))
            }
        }
    }

    // Upon observe, should emit a CL
    // that matches the previous positions of the currencies
    // Baseline positions should be reflected in CL lineup
    @Test
    fun Should_ReflectUserBaselineCurrencies_When_RatesAvailableButInDiffOrder() {
        // Given the ConversionList has already been updated,
        // And user has updated the baseline

        /* The rates we expect to be used by the VM for the sake of this test */
        val defaultRates = ratesDataSourceHarness.defaultRatesService.read().rates
        val defaultCurrencies = defaultSupportedCurrenciesService.read()
        val modifiedRatesOrder = defaultRates.toSortedMap(reverseOrder())
        val modifiedPositions =
            modifiedRatesOrder.keys.associateWith { modifiedRatesOrder.keys.indexOf(it) }
        val userUpdatedBaseline = UserBaselineInteractor.DEFAULT_BASELINE.copy(
            code = modifiedRatesOrder.firstKey(),
            name = defaultCurrencies.first { it.code == modifiedRatesOrder.firstKey() }.name,
            value = 1.0,
            positions = modifiedPositions
        )

        println("Old rates order: $defaultRates")
        println("New rates order: $modifiedRatesOrder")

        // User
        userBaselineInteractor.setUserBaseline(userUpdatedBaseline).blockingAwait()

        // When observe
        // Order of currency line items matches positions
        vm.conversionList.observeOnce { cl ->
            // Given the list of CLIs, we can do the same operations to generate a positions map,
            // as if this were a baseline update, and then compare to what our true position map
            // was when we updated the baseline

            val inferredPositions = cl.lineItems.associate { cli ->
                cli.code to cl.lineItems.indexOf(cli)
            }

            assertThat(
                inferredPositions,
                equalTo(modifiedPositions)
            )
        }
    }

    // FUTURE
    // Upon observe, should emit a CL
    // that matches previous positions,
    // and adds new currencies at bottom

    private fun convertValueFromDefaultRates(
        baselineCode: String,
        baselineValue: Double
    ): Map<String, Double> {
        val defaultRates = ratesDataSourceHarness.defaultRatesService.read().rates
        val converter = CurrencyConverter(defaultRates)

        return defaultRates.mapValues {
            converter.convert(baselineValue, baselineCode, it.key)
        }
    }
}

class TestRatesDataSourceHarness {
    private var ratesToStream: CurrencyRatesManifest? = null

    val networkRatesService = mock<CurrencyRatesService> {
        on { getRates(any()) } doAnswer {
            if (ratesToStream != null) Single.just(ratesToStream)
            else Single.error(Exception("Not found"))
        }
    }

    val memRatesStorage = MemCurrencyRatesStorage()
    val diskRatesStorage = PrefsCurrencyRatesStorage(TestSharedPrefs())
    val defaultRatesService = defaultCurrencyRatesService()

    val repository = CurrencyRatesRepositoryImpl(
        TestLogger(),

        // No need to deal w/ test scheduler & advancing time, etc. Just use trampoline
        TestSchedulerProvider(true),

        networkRatesService,
        diskRatesStorage,
        memRatesStorage,
        defaultRatesService
    )

    fun setRates(rates: CurrencyRatesManifest?) {
        this.ratesToStream = rates
        setMemRates(ratesToStream)
        setDiskRates(ratesToStream)
    }

    private fun setMemRates(rates: CurrencyRatesManifest?) {
        if (rates == null) this.memRatesStorage.clearRates().blockingAwait()
        else this.memRatesStorage.setRates(rates).blockingAwait()
    }

    private fun setDiskRates(rates: CurrencyRatesManifest?) {
        if (rates == null) this.diskRatesStorage.clearRates().blockingAwait()
        else this.diskRatesStorage.setRates(rates).blockingAwait()
    }

    fun clearAllRates() {
        this.ratesToStream = null
        this.memRatesStorage.clearRates().blockingAwait()
        this.diskRatesStorage.clearRates().blockingAwait()
    }
}