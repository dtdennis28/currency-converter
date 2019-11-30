package com.dtdennis.currency.ui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Cluster all the RV mgmt into a "coordinator" to keep activity clean
 */
class RecyclerViewCoordinator(
    private val recyclerView: RecyclerView,
    context: Context
) {
    private val adapter = ConvertedCurrencyRVAdapter()
    private val layoutManager = LinearLayoutManager(context)

    init {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    fun setItems(items: List<ConvertedCurrency>) = adapter.setItems(items)
}