package com.dtdennis.currency.util.mothers

import com.dtdennis.currency.data.currencies.DefaultSupportedCurrenciesService
import com.dtdennis.currency.data.currencies.SupportedCurrenciesRepositoryImpl
import com.dtdennis.currency.data.rates.DefaultCurrencyRatesService
import com.dtdennis.currency.util.MockAssetsViaResources

/**
 * For services that are backed by assets/.json files,
 * allow them to be used just the same but with a mock asset manager.
 * The mock asset manager is backed by an actual [File] from src/test/resources
 *
 * In other words, using the 'resources' folder as an assets folder and providing the same
 * input stream to these "default" services so they function the same way.
 *
 * If you wish to change the 'default' data that will be used in tests, change the .json
 * files in test/resources
 */
object DefaultServiceMothers {
    fun defaultSupportedCurrencyService() = DefaultSupportedCurrenciesService(
        MockAssetsViaResources.assetManager
    )

    fun defaultCurrencyRatesService() = DefaultCurrencyRatesService(
        MockAssetsViaResources.assetManager
    )
}