package com.dtdennis.currency.ui

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Cluster all the RV mgmt into a "coordinator" to keep activity clean
 */
class RecyclerViewCoordinator(
    private val recyclerView: RecyclerView,
    context: Context
) {
    private val adapter = ConvertedCurrencyRVAdapter(::onItemClicked)
    private val layoutManager = LinearLayoutManager(context)

    init {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    fun setItems(items: List<ConvertedCurrency>) = adapter.setItems(items)

    fun onItemClicked(position: Int, item: ConvertedCurrency, itemView: View) {
        val rearrangedList = adapter.items.toMutableList()
        rearrangedList.removeAt(position)
        rearrangedList.add(0, item)

        adapter.onItemMoved(rearrangedList, position, 0)
    }
}