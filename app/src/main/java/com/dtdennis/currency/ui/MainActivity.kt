package com.dtdennis.currency.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dtdennis.currency.CurrencyApplication
import com.dtdennis.currency.R
import com.dtdennis.currency.data.util.Logger
import com.dtdennis.currency.ui.entities.ConversionList
import com.dtdennis.currency.ui.entities.CurrencyLineItem
import com.dtdennis.currency.ui.entities.UserBaseline
import com.dtdennis.currency.ui.recyclerview.RecyclerViewCoordinator
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

        observeVM()
    }

    private fun observeVM() {
        viewModel.conversionList.observe(this,
            Observer<ConversionList> {
                if (it.lineItems.isNotEmpty()) {
                    hideInitialLoading()
                    recyclerViewCoordinator.setItems(it.lineItems)
                } else {
                    showInitialLoadingError()
                }
            })
    }

    private fun hideInitialLoading() {
        initial_loading_container.visibility = View.GONE
    }

    private fun showInitialLoadingError() {
        initial_loading_pb.visibility = View.GONE
        initial_text.text = "An error occurred. Please try again."
    }

    private fun onCurrenciesRearranged(newItems: List<CurrencyLineItem>) {
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
                base.name,
                base.value,
                positions
            )
        )
    }
}
