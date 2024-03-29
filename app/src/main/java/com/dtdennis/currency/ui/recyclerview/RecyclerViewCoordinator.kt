package com.dtdennis.currency.ui.recyclerview

import android.content.Context
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dtdennis.currency.ui.entities.CurrencyLineItem

/**
 * Cluster all the RV mgmt into a "coordinator" to keep activity clean.
 *
 * Used to orchestrate common RV tasks like onclick listeners and updating items
 */
private const val ANIMATION_WAIT_TIME = 500L

class RecyclerViewCoordinator(
    private val onItemsRearrangedListener: (newItems: List<CurrencyLineItem>) -> Unit,
    recyclerView: RecyclerView,
    context: Context
) {
    private val adapter = CurrencyLineItemRVAdapter(
        ::onItemClicked,
        ::onItemValueChanged
    )
    private val layoutManager = LinearLayoutManager(context)

    // Crude way to prevent item changes while animating
    private var isAwaitingMoveAnimation = false
    private val animationHandler = Handler()

    init {
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    fun setItems(items: List<CurrencyLineItem>) {
        if(!isAwaitingMoveAnimation) adapter.setItems(items)
    }

    private fun onItemClicked(position: Int, item: CurrencyLineItem, itemView: View) {
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

    private fun onItemValueChanged(position: Int, item: CurrencyLineItem, value: Double) {
        val newItems = adapter.items.toMutableList()
        newItems.removeAt(0)
        newItems.add(0, item.copy(value = value))

        onItemsRearrangedListener(newItems)
    }
}