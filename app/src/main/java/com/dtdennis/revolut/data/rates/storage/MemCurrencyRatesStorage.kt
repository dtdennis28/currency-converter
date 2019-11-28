package com.dtdennis.revolut.data.rates.storage

import com.dtdennis.revolut.core.rates.CurrencyRatesManifest
import io.reactivex.Completable
import io.reactivex.Maybe

class MemCurrencyRatesStorage : CurrencyRatesStorage {
    private var rates: CurrencyRatesManifest? = null

    override fun setRates(currencyRatesManifest: CurrencyRatesManifest): Completable =
        Completable.fromAction {
            this.rates = currencyRatesManifest
        }

    override fun getRates(): Maybe<CurrencyRatesManifest> =
        if (rates != null) Maybe.just(rates)
        else Maybe.empty()

    override fun clearRates(): Completable =
        Completable.fromAction {
            this.rates = null
        }
}