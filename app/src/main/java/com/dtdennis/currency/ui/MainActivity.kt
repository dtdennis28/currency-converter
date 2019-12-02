package com.dtdennis.currency.ui

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dtdennis.currency.CurrencyApplication
import com.dtdennis.currency.R
import com.dtdennis.currency.data.util.Logger
import com.dtdennis.currency.ui.util.LiveDataEvent
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

        val immediateConverionList = viewModel.onBaselineChanged(
            UserBaseline(
                base.code,
                base.name,
                base.value,
                positions
            )
        )

        // We have an immediate conversion to display
        // Posting w/ delay because otherwise it will try to call "setItems" while recyclerview
        // is still scrolling / animating
        if (immediateConverionList != null) {
            Handler().postDelayed(
                {
                    recyclerViewCoordinator.setItems(immediateConverionList.lineItems)
                },
                1
            )
        }
    }
}
