package com.dtdennis.currency

import android.app.Application
import com.dtdennis.currency.internal.di.CurrencyAppComponent
import com.dtdennis.currency.internal.di.CurrencyAppModule
import com.dtdennis.currency.internal.di.DaggerCurrencyAppComponent

class CurrencyApplication : Application() {
    companion object {
        private var component: CurrencyAppComponent? = null
        fun component(): CurrencyAppComponent {
            return component!!
        }
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerCurrencyAppComponent
            .builder()
            .currencyAppModule(CurrencyAppModule(this))
            .build()
    }
}