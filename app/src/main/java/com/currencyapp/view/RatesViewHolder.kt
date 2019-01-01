package com.currencyapp.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.currencyapp.R
import com.currencyapp.model.CurrencyRate
import java.text.DecimalFormat

class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameView: TextView? = itemView.findViewById(R.id.txtCurrencyName)
    private val rateView: TextView? = itemView.findViewById(R.id.etCurrencyValue)
    private val rateFormat = DecimalFormat("#.00")

    fun bind(
        rates: CurrencyRate,
        currentInputValue: Double,
        rateChangeView: RateChangeView?
    ) {
        nameView?.text = rates.name
        rateView?.text = rateFormat.format(rates.rate * currentInputValue)
        val rateChangePresenter = RateChangePresenter(rateChangeView!!, rates)
        rateView?.onFocusChangeListener = rateChangePresenter
        rateView?.setOnEditorActionListener(rateChangePresenter)
        rateView?.addTextChangedListener(rateChangePresenter)
    }
}