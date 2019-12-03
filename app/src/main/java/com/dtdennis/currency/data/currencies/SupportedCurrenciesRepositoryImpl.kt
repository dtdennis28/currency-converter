package com.dtdennis.currency.data.currencies

import com.dtdennis.currency.core.currencies.Currency
import com.dtdennis.currency.core.currencies.SupportedCurrenciesRepository
import io.reactivex.Single

/**
 * Reads the bundled JSON file for a list of supported currencies
 *
 * Can be API-backed in future.
 */
class SupportedCurrenciesRepositoryImpl(
    private val serviceDefault: DefaultSupportedCurrenciesService
) : SupportedCurrenciesRepository {
    // Keep in memory ref for perf
    private var supportedCurrencies: List<Currency>? = null

    override fun getSupportedCurrencies(): Single<List<Currency>> =
        Single.fromCallable {
            if (supportedCurrencies == null)
                supportedCurrencies = serviceDefault.read()

            supportedCurrencies
        }
}