package com.currencyapp.view

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import com.currencyapp.R
import com.currencyapp.model.CurrencyRate

class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var name: TextView? = null
    private var rate: TextView? = null

    fun bind(rates: CurrencyRate, currentInputValue: Float) {
        name = itemView.findViewById(R.id.txtCurrencyName)
        rate = itemView.findViewById(R.id.etCurrencyValue)

        name?.text = rates.name
        val totalValue: Float = rates.rate * currentInputValue
        rate?.text = totalValue.toString()

        rate?.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                run {
                    Log.d("TEST", "has Focus? ${hasFocus}")
                }
            }
    }
}