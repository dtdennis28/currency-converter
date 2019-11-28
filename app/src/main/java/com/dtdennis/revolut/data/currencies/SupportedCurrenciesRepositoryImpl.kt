package com.dtdennis.revolut.data.currencies

import com.dtdennis.revolut.core.currencies.Currency
import com.dtdennis.revolut.core.currencies.SupportedCurrenciesRepository
import io.reactivex.Single

class SupportedCurrenciesRepositoryImpl(
    private val service: SupportedCurrenciesLocalService
) : SupportedCurrenciesRepository {
    override fun getSupportedCurrencies(): Single<List<Currency>> =
        Single.fromCallable {
            service.getSupportedCurrencies()
        }
}