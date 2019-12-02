package com.dtdennis.currency.ui

import android.content.Context
import android.view.View
import android.widget.NumberPicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Cluster all the RV mgmt into a "coordinator" to keep activity clean
 */
class RecyclerViewCoordinator(
    private val recyclerView: RecyclerView,
    private val onItemsRearrangedListener: (newItems: List<ConvertedCurrency>) -> Unit,
    context: Context
) {
    private val adapter = ConvertedCurrencyRVAdapter(::onItemClicked, ::onItemValueChanged)
    private val layoutManager = LinearLayoutManager(context)

    init {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    fun setItems(items: List<ConvertedCurrency>) = adapter.setItems(items)

    private fun onItemClicked(position: Int, item: ConvertedCurrency, itemView: View) {
        val rearrangedList = adapter.items.toMutableList()
        rearrangedList.removeAt(position)
        rearrangedList.add(0, item)

        adapter.onItemMoved(rearrangedList, position, 0)
        onItemsRearrangedListener(rearrangedList)
    }

    private fun onItemValueChanged(position: Int, item: ConvertedCurrency, value: Double) {
        val newItems = adapter.items.toMutableList()
        newItems.removeAt(0)
        newItems.add(0, item.copy(value = value))

        onItemsRearrangedListener(newItems)
    }
}