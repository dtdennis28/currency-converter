package com.dtdennis.currency.ui

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.NumberPicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * Cluster all the RV mgmt into a "coordinator" to keep activity clean
 */
private const val ANIMATION_WAIT_TIME = 500L

class RecyclerViewCoordinator(
    private val recyclerView: RecyclerView,
    private val onItemsRearrangedListener: (newItems: List<ConvertedCurrency>) -> Unit,
    context: Context
) {
    private val adapter = ConvertedCurrencyRVAdapter(::onItemClicked)
    private val layoutManager = LinearLayoutManager(context)

    // Crude way to prevent item changes while animating
    private var isAwaitingMoveAnimation = false
    private val animationHandler = Handler()

    init {
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    fun setItems(items: List<ConvertedCurrency>) {
        if(!isAwaitingMoveAnimation) adapter.setItems(items)
    }

    private fun onItemClicked(position: Int, item: ConvertedCurrency, itemView: View) {
        val rearrangedList = adapter.items.toMutableList()
        rearrangedList.removeAt(position)
        rearrangedList.add(0, item)

        adapter.onItemMoved(rearrangedList, position, 0)
        onItemsRearrangedListener(rearrangedList)

        isAwaitingMoveAnimation = true
        animationHandler.postDelayed({
            isAwaitingMoveAnimation = false
        }, ANIMATION_WAIT_TIME)
    }

    private fun onItemValueChanged(position: Int, item: ConvertedCurrency, value: Double) {
        val newItems = adapter.items.toMutableList()
        newItems.removeAt(0)
        newItems.add(0, item.copy(value = value))

        onItemsRearrangedListener(newItems)
    }
}