package com.dtdennis.currency.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dtdennis.currency.R

class ConvertedCurrencyRVAdapter : RecyclerView.Adapter<ConvertedCurrencyRVAdapter.ConvertedCurrencyVH>() {
    private var items: List<ConvertedCurrency> = emptyList()

    class ConvertedCurrencyVH(val itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount() = items.size

    fun setItems(items: List<ConvertedCurrency>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertedCurrencyVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_currency, parent, false)
        return ConvertedCurrencyVH(itemView)
    }

    override fun onBindViewHolder(holder: ConvertedCurrencyVH, position: Int) {

    }
}