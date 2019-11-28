package com.dtdennis.revolut.data.rates.storage

import com.dtdennis.revolut.core.rates.CurrencyRatesManifest
import io.reactivex.Completable
import io.reactivex.Maybe

interface CurrencyRatesStorage {
    fun setRates(currencyRatesManifest: CurrencyRatesManifest): Completable
    fun getRates(): Maybe<CurrencyRatesManifest>
    fun clearRates(): Completable
}