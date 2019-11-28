package com.dtdennis.revolut.data.rates

import com.dtdennis.revolut.core.rates.CurrencyRatesManifest
import io.reactivex.Flowable
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