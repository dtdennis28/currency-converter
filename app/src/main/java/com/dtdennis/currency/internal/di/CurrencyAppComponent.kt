package com.dtdennis.currency.internal.di

import com.dtdennis.currency.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [CurrencyAppModule::class]
)
@Singleton
interface CurrencyAppComponent {
    fun inject(mainActivity: MainActivity)
}