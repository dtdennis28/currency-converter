package com.dtdennis.currency.data.rates.storage

import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import io.reactivex.Completable
import io.reactivex.Maybe

interface CurrencyRatesStorage {
    fun setRates(currencyRatesManifest: CurrencyRatesManifest): Completable
    fun getRates(): Maybe<CurrencyRatesManifest>
    fun clearRates(): Completable
}