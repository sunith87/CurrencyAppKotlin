package com.currencyapp.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.currencyapp.R
import com.currencyapp.model.CurrencyRate

class RatesAdapter : RecyclerView.Adapter<RatesViewHolder>() {

    var rates: List<CurrencyRate>? = ArrayList()
        set(rates) {
            field = rates
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return rates?.size ?: 0
    }

    override fun onBindViewHolder(ratesViewHolder: RatesViewHolder, position: Int) {
        rates?.let {
            ratesViewHolder.bind(it.get(position), 1F)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RatesViewHolder {
        val inflatedView = LayoutInflater.from(viewGroup.context).inflate(R.layout.currency_item_layout, viewGroup, false)
        return RatesViewHolder(inflatedView)
    }

}