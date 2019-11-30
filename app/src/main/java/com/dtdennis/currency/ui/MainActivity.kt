package com.dtdennis.currency.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dtdennis.currency.CurrencyApplication
import com.dtdennis.currency.R
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: MainVM

    private lateinit var recyclerViewCoordinator: RecyclerViewCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CurrencyApplication.component().inject(this)

        setContentView(R.layout.activity_main)

        recyclerViewCoordinator = RecyclerViewCoordinator(currencies_rv, this)

        viewModel.convertedCurrencies.observe(this,
            Observer<List<ConvertedCurrency>> {
                recyclerViewCoordinator.setItems(it)
            })
    }
}
