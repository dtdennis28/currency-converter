package com.dtdennis.currency.data.rates

import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit-powered service for fetching the rates from the "latest" API.
 *
 * E.g. https://revolut.duckdns.org/latest?base=EUR
 */
interface CurrencyRatesService {
    @GET("/latest")
    fun getRates(@Query("base") base: String): Single<CurrencyRatesManifest>
}