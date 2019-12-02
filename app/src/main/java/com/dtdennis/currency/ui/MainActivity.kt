package com.dtdennis.currency.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dtdennis.currency.CurrencyApplication
import com.dtdennis.currency.R
import com.dtdennis.currency.data.util.Logger
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var viewModel: MainVM

    private lateinit var recyclerViewCoordinator: RecyclerViewCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CurrencyApplication.component().inject(this)

        setContentView(R.layout.activity_main)

        recyclerViewCoordinator = RecyclerViewCoordinator(
            currencies_rv,
            ::onCurrenciesRearranged,
            this
        )

        viewModel.conversionList.observe(this,
            Observer<ConversionList> {
                logger.d(TAG, "New conv list: $it")
                recyclerViewCoordinator.setItems(
                    it.lineItems.map {
                        ConvertedCurrency(it.code, it.name, it.value)
                    }
                )
            })
    }

    private fun onCurrenciesRearranged(newItems: List<ConvertedCurrency>) {
        val base = newItems[0]
        val positions =
            newItems.associate {
                Pair(
                    it.code,
                    newItems.indexOf(it)
                )
            }

        viewModel.onBaselineChanged(
            UserBaseline(
                base.code,
                base.value,
                positions
            )
        )
    }
}
