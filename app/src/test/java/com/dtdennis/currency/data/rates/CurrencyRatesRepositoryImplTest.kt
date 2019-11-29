package com.dtdennis.currency.data.rates

import com.dtdennis.currency.util.mothers.CurrencyRatesManifestMother
import com.dtdennis.currency.util.mothers.LoggerMother.TEST_LOGGER
import com.dtdennis.currency.util.mothers.SchedulerProviderMother
import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import com.dtdennis.currency.data.rates.storage.MemCurrencyRatesStorage
import com.dtdennis.currency.data.rates.storage.PrefsCurrencyRatesStorage
import com.dtdennis.currency.util.TestSharedPrefs
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subscribers.TestSubscriber
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class CurrencyRatesRepositoryImplTest {
    private val testNetworkRates = CurrencyRatesManifestMother.defaultTestCurrencyRatesManifest()
    private val testDiskRates = testNetworkRates.copy(date = testNetworkRates.date + "-disk")
    private val testMemRates = testNetworkRates.copy(date = testNetworkRates.date + "-mem")

    private val testDataSource = TestDataSourceHarness()

    private val testSchedulerProvider = SchedulerProviderMother.testSchedulerProvider()
    private val testScheduler = testSchedulerProvider.io

    private val repo = CurrencyRatesRepositoryImpl(
        TEST_LOGGER,
        testSchedulerProvider,
        testDataSource.networkRatesService,
        testDataSource.diskRatesStorage,
        testDataSource.memRatesStorage
    )

    @Before
    fun setUp() {
        setDefaultRates()
    }

    @Test
    fun Should_StreamRatesFromServerEverySecond() {
        // Given data source is available
        // When streaming rates
        // Then receive a new manifest every second
        val subscriber = testStream(testNetworkRates.base)

        for (i in 0..10) {
            testScheduler.advance()
            subscriber.assertValueCount(i + 1)
        }
    }

    @Test
    fun Should_SourceDisk_When_ServiceUnavailable_And_MemUnavailable() {
        // Given service is not available
        testDataSource.setNetworkRates(null)
        testDataSource.setMemRates(null)

        // When streaming rates
        val subscriber = testStream(testNetworkRates.base)
        testScheduler.advance()

        // Then receive the cached manifest
        subscriber.assertValue(testDiskRates)
    }

    @Test
    fun Should_SourceMemory_When_ServiceUnavailable() {
        // Given service is not available
        testDataSource.setNetworkRates(null)

        // When streaming
        val subscriber = testStream(testNetworkRates.base)
        testScheduler.advance()

        // Then receive in-mem manifest
        subscriber.assertValue(testMemRates)
    }

    @Test
    fun Should_StoreRatesToDisk_When_RatesUpdated() {
        // Given no mem or disk rates, but service is available,
        testDataSource.clearAllRates()
        testDataSource.setNetworkRates(testNetworkRates)

        // When stream rates
        testStream(testNetworkRates.base)
        testScheduler.advance()

        // Then rates stored
        assertThat(testDataSource.diskRatesStorage.getRates().blockingGet(), equalTo(testNetworkRates))
    }

    @Test
    fun Should_StoreRatesToMemory_When_RatesUpdated() {
        // Given no mem or disk rates, but service is available,
        testDataSource.clearAllRates()
        testDataSource.setNetworkRates(testNetworkRates)

        // When stream rates
        testStream(testNetworkRates.base)
        testScheduler.advance()

        // Then rates stored
        assertThat(testDataSource.memRatesStorage.getRates().blockingGet(), equalTo(testNetworkRates))
    }

    @Test
    fun Should_FetchFromDisk_When_MemoryCleared_And_ServiceUnavailable() {
        // Given new rates fetched from network at some previous point
        // and that the app has been cleared from memory
        // and now service is unavailable
        testDataSource.clearAllRates()

        /* Fetch new rates */
        val newRates = testNetworkRates.copy(date = testNetworkRates.date + "+1")
        testDataSource.setNetworkRates(newRates)
        testStream(testNetworkRates.base)
        testScheduler.advance()

        /* Make service unavailable */
        testDataSource.setNetworkRates(null)

        /* Clear app from memory */
        testDataSource.setMemRates(null)

        // When rates are fetched again
        val subscriber = testStream(testNetworkRates.base)
        testScheduler.advance()

        // Then the latest rates are still obtained (because they were stored in disk)
        subscriber.assertValue(newRates)
    }

    private fun setDefaultRates() {
        testDataSource.setNetworkRates(testNetworkRates)
        testDataSource.setDiskRates(testDiskRates)
        testDataSource.setMemRates(testMemRates)
    }

    private fun testStream(baseCurrency: String): TestSubscriber<CurrencyRatesManifest> {
        return repo
            .streamRates(baseCurrency)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()
    }

    /**
     * Ext function to simplify advancing the 1-second increments
     */
    private fun TestScheduler.advance() {
        this.advanceTimeBy(1L, TimeUnit.SECONDS)
    }

    private class TestDataSourceHarness {
        private var networkRates: CurrencyRatesManifest? = null

        val networkRatesService = mock<CurrencyRatesService> {
            on { getRates(any()) } doAnswer {
                if (networkRates != null) Single.just(networkRates)
                else Single.error(Exception("Not found"))
            }
        }

        val memRatesStorage = MemCurrencyRatesStorage()
        val diskRatesStorage = PrefsCurrencyRatesStorage(TestSharedPrefs(), Gson())

        fun setNetworkRates(rates: CurrencyRatesManifest?) {
            this.networkRates = rates
        }

        fun setMemRates(rates: CurrencyRatesManifest?) {
            if (rates == null) this.memRatesStorage.clearRates().blockingAwait()
            else this.memRatesStorage.setRates(rates).blockingAwait()
        }

        fun setDiskRates(rates: CurrencyRatesManifest?) {
            if (rates == null) this.diskRatesStorage.clearRates().blockingAwait()
            else this.diskRatesStorage.setRates(rates).blockingAwait()
        }

        fun clearAllRates() {
            this.networkRates = null
            this.memRatesStorage.clearRates().blockingAwait()
            this.diskRatesStorage.clearRates().blockingAwait()
        }
    }
}