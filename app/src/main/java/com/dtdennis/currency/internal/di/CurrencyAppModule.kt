package com.dtdennis.currency.internal.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.dtdennis.currency.core.currencies.SupportedCurrenciesRepository
import com.dtdennis.currency.core.rates.CurrencyRatesRepository
import com.dtdennis.currency.data.currencies.DefaultSupportedCurrenciesService
import com.dtdennis.currency.data.currencies.SupportedCurrenciesRepositoryImpl
import com.dtdennis.currency.data.rates.CurrencyRatesRepositoryImpl
import com.dtdennis.currency.data.rates.CurrencyRatesService
import com.dtdennis.currency.data.rates.DefaultCurrencyRatesService
import com.dtdennis.currency.data.rates.storage.MemCurrencyRatesStorage
import com.dtdennis.currency.data.rates.storage.PrefsCurrencyRatesStorage
import com.dtdennis.currency.data.util.Logger
import com.dtdennis.currency.data.util.SchedulerProvider
import com.dtdennis.currency.internal.retrofit.RetrofitInstanceHolder
import com.dtdennis.currency.util.AppLogger
import com.dtdennis.currency.util.AppSchedulerProvider
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CurrencyAppModule(private val app: Application) {
    @Provides
    fun provideContext(): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return AppLogger()
    }

    @Provides
    fun provideSupportedCurrenciesService(context: Context): DefaultSupportedCurrenciesService {
        return DefaultSupportedCurrenciesService(context.assets)
    }

    @Provides
    fun provideSupportedCurrenciesRepository(defaultSupportedCurrenciesService: DefaultSupportedCurrenciesService): SupportedCurrenciesRepository {
        return SupportedCurrenciesRepositoryImpl(defaultSupportedCurrenciesService)
    }

    @Provides
    fun provideDefaultCurrencyRatesServie(context: Context): DefaultCurrencyRatesService {
        return DefaultCurrencyRatesService(context.assets)
    }

    @Provides
    fun provideCurrencyRatesRepository(
        logger: Logger,
        schedulerProvider: SchedulerProvider,
        currencyRatesService: CurrencyRatesService,
        diskStorage: PrefsCurrencyRatesStorage,
        memoryStorage: MemCurrencyRatesStorage,
        defaultCurrencyRatesService: DefaultCurrencyRatesService
    ): CurrencyRatesRepository {
        return CurrencyRatesRepositoryImpl(
            logger,
            schedulerProvider,
            currencyRatesService,
            diskStorage,
            memoryStorage,
            defaultCurrencyRatesService
        )
    }

    @Provides
    fun provideCurrencyRatesService(): CurrencyRatesService {
        return RetrofitInstanceHolder.instance.create(CurrencyRatesService::class.java)
    }

    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}