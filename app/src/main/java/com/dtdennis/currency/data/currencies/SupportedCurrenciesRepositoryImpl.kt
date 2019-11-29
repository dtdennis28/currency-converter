package com.dtdennis.currency.data.currencies

import com.dtdennis.currency.core.currencies.Currency
import com.dtdennis.currency.core.currencies.SupportedCurrenciesRepository
import io.reactivex.Single

class SupportedCurrenciesRepositoryImpl(
    private val service: SupportedCurrenciesLocalService
) : SupportedCurrenciesRepository {
    override fun getSupportedCurrencies(): Single<List<Currency>> =
        Single.fromCallable {
            service.getSupportedCurrencies()
        }
}