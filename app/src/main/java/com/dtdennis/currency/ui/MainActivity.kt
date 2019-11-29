package com.dtdennis.currency.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dtdennis.currency.CurrencyApplication
import com.dtdennis.currency.R
import com.dtdennis.currency.core.rates.CurrencyRatesInteractor
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: MainVM

    @Inject
    lateinit var ratesInteractor: CurrencyRatesInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CurrencyApplication.component().inject(this)

        setContentView(R.layout.activity_main)

        ratesInteractor
            .streamRates("EUR")
            .subscribeOn(Schedulers.newThread())
            .doOnNext {
                Log.d("MainActivity", "On next: $it")
            }
            .doOnError {
                Log.e("MainActivity", "Error", it)
            }
            .subscribe()
    }
}
