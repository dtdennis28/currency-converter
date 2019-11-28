package com.dtdennis.revolut.data.currencies

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Simple Robolectric test to verify JSON parsing
 *
 */
@RunWith(RobolectricTestRunner::class)
class SupportedCurrenciesLocalServiceTest {
    @Test
    fun Should_ParseSupportedCurrenciesList() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val localService = SupportedCurrenciesLocalService(context.assets)

        val supportedCurrencies = localService.getSupportedCurrencies()
        println(supportedCurrencies)
        assertThat(supportedCurrencies.isNullOrEmpty(), equalTo(false))
    }
}