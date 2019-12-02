package com.dtdennis.currency.data.currencies

import com.dtdennis.currency.core.currencies.Currency
import com.dtdennis.currency.core.currencies.SupportedCurrenciesRepository
import io.reactivex.Single

class SupportedCurrenciesRepositoryImpl(
    private val service: SupportedCurrenciesLocalService
) : SupportedCurrenciesRepository {
    // Keep in memory ref for perf
    private var supportedCurrencies: List<Currency>? = null

    override fun getSupportedCurrencies(): Single<List<Currency>> =
        Single.fromCallable {
            if (supportedCurrencies == null)
                supportedCurrencies = service.getSupportedCurrencies()

            supportedCurrencies
        }
}