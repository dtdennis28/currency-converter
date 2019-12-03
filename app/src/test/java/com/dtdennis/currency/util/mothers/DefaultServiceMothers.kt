package com.dtdennis.currency.util.mothers

import com.dtdennis.currency.data.currencies.DefaultSupportedCurrenciesService
import com.dtdennis.currency.data.currencies.SupportedCurrenciesRepositoryImpl
import com.dtdennis.currency.data.rates.DefaultCurrencyRatesService
import com.dtdennis.currency.util.MockAssetsViaResources

object DefaultServiceMothers {
    fun defaultSupportedCurrencyService() = DefaultSupportedCurrenciesService(
        MockAssetsViaResources.assetManager
    )

    fun defaultCurrencyRatesService() = DefaultCurrencyRatesService(
        MockAssetsViaResources.assetManager
    )

    fun supportedCurrenciesRepository() = SupportedCurrenciesRepositoryImpl(
        defaultSupportedCurrencyService()
    )
}