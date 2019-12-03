package com.dtdennis.currency

import android.app.Application
import com.dtdennis.currency.internal.di.CurrencyAppComponent
import com.dtdennis.currency.internal.di.CurrencyAppModule
import com.dtdennis.currency.internal.di.DaggerCurrencyAppComponent
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

/**
 * Base application.
 */
private const val APPCENTER_KEY = "f068adaa-a82c-448f-add6-a4c3d9eb4be5"

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

        AppCenter.start(
            this,
            APPCENTER_KEY,
            Analytics::class.java,
            Crashes::class.java
        )
    }
}