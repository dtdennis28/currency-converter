package com.dtdennis.currency.internal.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Simple reference to a global Retrofit instance
 */
object RetrofitInstanceHolder {
    private const val BASE_URL = "https://revolut.duckdns.org"

    val instance by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}