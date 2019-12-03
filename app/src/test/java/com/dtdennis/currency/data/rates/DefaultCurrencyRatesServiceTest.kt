package com.dtdennis.currency.data.rates

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.dtdennis.currency.data.currencies.DefaultSupportedCurrenciesService
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DefaultCurrencyRatesServiceTest {
    val assets = ApplicationProvider.getApplicationContext<Context>().assets
    val defaultRatesService = DefaultCurrencyRatesService(assets)
    val defaultSupportedCurrenciesService = DefaultSupportedCurrenciesService(assets)

    @Test
    fun Should_ParseDefaultCurrencyRateManifest() {
        val defaultManifest = defaultRatesService.read()
        println(defaultManifest)

        assertThat(defaultManifest, notNullValue())
    }

    /**
     * For the defaults, we want parity between the supported currencies list and the default rates,
     * just for clarity and sanity.
     */
    @Test
    fun Should_MatchSupportedCurrencies() {
        val defaultManifest = defaultRatesService.read()
        val defaultSupportedCurrenciesMap =
            defaultSupportedCurrenciesService.read().associateBy { it.code }

        defaultSupportedCurrenciesMap.forEach {
            assertThat(defaultManifest.rates.containsKey(it.key), equalTo(true))
        }

        defaultManifest.rates.forEach {
            assertThat(defaultSupportedCurrenciesMap.containsKey(it.key), equalTo(true))
        }
    }
}