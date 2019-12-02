package com.dtdennis.currency.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dtdennis.currency.R

class ConvertedCurrencyRVAdapter(
    private val onItemClickListener: (position: Int, item: ConvertedCurrency, itemView: View) -> Unit,
    private val onValueChangeListener: (position: Int, item: ConvertedCurrency, value: Double) -> Unit
) : RecyclerView.Adapter<ConvertedCurrencyRVAdapter.ConvertedCurrencyVH>() {
    var items: List<ConvertedCurrency> = emptyList()
        private set

    class ConvertedCurrencyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyCodeTV = itemView.findViewById<TextView>(R.id.currency_code_tv)
        val currencyNameTV = itemView.findViewById<TextView>(R.id.currency_name_tv)
        val conversionET = itemView.findViewById<EditText>(R.id.conversion_et)

        var textWatcher: TextWatcher? = null
    }

    class ValueTextWatcher(
        private val valueChangeCallback: (newValue: Double) -> Unit
    ) : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val newValue = if (p0.isNullOrBlank()) {
                0.0
            } else {
                p0.toString().toDouble()
            }

            valueChangeCallback(newValue)
        }
    }

    override fun getItemCount() = items.size

    /**
     * Full re-set of the list (e.g. backing list & view re-render)
     */
    fun setItems(items: List<ConvertedCurrency>) {
        this.items = items

        // Use notifyItemChanged over notifyDataSetChanged to keep from re-render glitches
        // Note: this will fail if the back-end adds new currencies into the list
        this.items.forEachIndexed { index, item ->
            notifyItemChanged(index)
        }
    }

    /**
     * Only notify the adapter about the moved item, but also re-set the backing list
     * Will maintain all views already bound, but re-bind the rearranged item
     */
    fun onItemMoved(newItems: List<ConvertedCurrency>, fromPosition: Int, toPosition: Int) {
        this.items = newItems
        notifyItemMoved(fromPosition, toPosition)
        this.items.forEachIndexed { index, item ->
            notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertedCurrencyVH {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_currency, parent, false)
        return ConvertedCurrencyVH(itemView)
    }

    override fun onBindViewHolder(holder: ConvertedCurrencyVH, position: Int) {
        val item = items[position]

        holder.currencyCodeTV.text = item.code
        holder.currencyNameTV.text = item.name

        if (position == 0) bindFirstItem(position, item, holder)
        else bindNonFirstItem(position, item, holder)
    }

    private fun bindFirstItem(
        position: Int,
        item: ConvertedCurrency,
        holder: ConvertedCurrencyVH
    ) {
        holder.conversionET.setOnTouchListener(null)
        holder.itemView.setOnClickListener(null)

        if (!holder.conversionET.hasFocus()) {
            holder.conversionET.setText(String.format("%.3f", item.value))
            holder.conversionET.setSelection(holder.conversionET.text.length)
        }

        if (holder.textWatcher == null) {
            holder.textWatcher = ValueTextWatcher {
                onValueChangeListener(position, item, it)
            }
            holder.conversionET.addTextChangedListener(holder.textWatcher)
        }
    }

    private fun bindNonFirstItem(
        position: Int,
        item: ConvertedCurrency,
        holder: ConvertedCurrencyVH
    ) {
        if(holder.textWatcher != null) {
            holder.conversionET.removeTextChangedListener(holder.textWatcher)
            holder.textWatcher = null
        }

        holder.conversionET.setText(String.format("%.3f", item.value))

        // Override on-touch action-up to treat the "click" on the ET the same as an item click
        // This will leave it so that all clicks, regardless of which child item is touched,
        // are treated the same (e.g. to rearrange items upon click, to focus the ET, etc.)
        holder.conversionET.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                holder.itemView.performClick()
                true
            }

            false
        }

        holder.itemView.setOnClickListener {
            onItemClickListener(position, item, holder.itemView)

            // The only reason one would click the row would be to edit its conversion ET,
            // So immediately request focus
            holder.conversionET.requestFocus()
            holder.conversionET.setSelection(holder.conversionET.text.length)
        }
    }
}