package com.dtdennis.currency.ui.recyclerview

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dtdennis.currency.R
import com.dtdennis.currency.core.currencies.CurrencyIcon
import com.dtdennis.currency.ui.CurrencyValueFormatter
import com.dtdennis.currency.ui.entities.CurrencyLineItem
import com.dtdennis.currency.ui.util.ResourceIdFetcher
import com.squareup.picasso.Picasso

class CurrencyLineItemRVAdapter(
    private val onItemClickListener: (position: Int, item: CurrencyLineItem, itemView: View) -> Unit,
    private val onValueChangeListener: (position: Int, item: CurrencyLineItem, value: Double) -> Unit
) : RecyclerView.Adapter<CurrencyLineItemRVAdapter.CurrencyLineItemVH>() {
    private val picasso = Picasso.get()

    var items: List<CurrencyLineItem> = emptyList()
        private set

    class CurrencyLineItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyIconIV = itemView.findViewById<ImageView>(R.id.currency_icon_iv)
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
            val newValue = try {
                p0?.toString()?.toDouble() ?: throw Exception()
            } catch (error: Throwable) {
                0.0
            }

            valueChangeCallback(newValue)
        }
    }

    override fun getItemCount() = items.size

    /**
     * Full re-set of the list (e.g. backing list & view re-render)
     */
    fun setItems(items: List<CurrencyLineItem>) {
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
    fun onItemMoved(newItems: List<CurrencyLineItem>, fromPosition: Int, toPosition: Int) {
        this.items = newItems
        notifyItemMoved(fromPosition, toPosition)
        this.items.forEachIndexed { index, item ->
            notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyLineItemVH {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_currency, parent, false)
        return CurrencyLineItemVH(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyLineItemVH, position: Int) {
        val item = items[position]

        bindBasics(item, holder)

        if (position == 0) bindFirstItem(position, item, holder)
        else bindNonFirstItem(position, item, holder)
    }

    private fun bindFirstItem(
        position: Int,
        item: CurrencyLineItem,
        holder: CurrencyLineItemVH
    ) {
        holder.conversionET.setOnTouchListener(null)
        holder.itemView.setOnClickListener(null)

        holder.conversionET.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                bindValue(item.value, holder)
                holder.conversionET.setTextColor(Color.BLACK)
                hideKeyboard(holder.conversionET)
            } else {
                holder.conversionET.setTextColor(holder.itemView.resources.getColor(R.color.colorAccent))
            }
        }

        if (!holder.conversionET.hasFocus()) {
            bindValue(item.value, holder)
            holder.conversionET.setTextColor(Color.BLACK)
            holder.conversionET.setSelection(holder.conversionET.text.length)
        } else {
            holder.conversionET.setTextColor(holder.itemView.resources.getColor(R.color.colorAccent))
        }

        if (holder.textWatcher == null) {
            holder.textWatcher =
                ValueTextWatcher {
                    onValueChangeListener(position, item, it)
                }
            holder.conversionET.addTextChangedListener(holder.textWatcher)
        }
    }

    private fun hideKeyboard(editText: View) {
        val imm =
            editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun bindNonFirstItem(
        position: Int,
        item: CurrencyLineItem,
        holder: CurrencyLineItemVH
    ) {
        if (holder.textWatcher != null) {
            holder.conversionET.removeTextChangedListener(holder.textWatcher)
            holder.textWatcher = null
        }

        holder.conversionET.onFocusChangeListener = null

        bindValue(item.value, holder)
        holder.conversionET.setTextColor(Color.BLACK)

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
            holder.conversionET.setTextColor(holder.itemView.resources.getColor(R.color.colorAccent))
        }
    }

    private fun bindBasics(
        item: CurrencyLineItem,
        holder: CurrencyLineItemVH
    ) {
        bindIcon(item, holder)
        holder.currencyCodeTV.text = item.code
        holder.currencyNameTV.text = item.name
    }

    private fun bindIcon(item: CurrencyLineItem, holder: CurrencyLineItemVH) {
        // Load the icon based on local or remote
        if (item.icon.type == CurrencyIcon.Type.LOCAL) {
            picasso.load(
                ResourceIdFetcher.getDrawableResIdFromResName(
                    holder.itemView.context,
                    item.icon.location,
                    R.drawable.ic_currency_generic
                )
            )
                .fit()
                .into(holder.currencyIconIV)
        } else {
            picasso.load(item.icon.location).into(holder.currencyIconIV)
        }
    }

    private fun bindValue(value: Double, holder: CurrencyLineItemVH) {
        holder.conversionET.setText(CurrencyValueFormatter.format(value))
    }
}