package com.currencyapp.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.currencyapp.R
import com.currencyapp.model.CurrencyRate

class RatesAdapter : RecyclerView.Adapter<RatesViewHolder>() {

    var rates: MutableList<CurrencyRate>? = ArrayList()
        set(value) {
            when (hasFocus) {
                false -> {
                    if (field != null && field!!.isEmpty()) {
                        field = value
                    } else {
                        field = keepOrderOfListAndMutate(field, value)
                    }
                    notifyDataSetChanged()
                }
            }
        }

    private fun keepOrderOfListAndMutate(
        cachedRates: MutableList<CurrencyRate>?,
        newRates: MutableList<CurrencyRate>?
    ): MutableList<CurrencyRate> {
        val mutatedRates: MutableList<CurrencyRate> = ArrayList()
        cachedRates?.mapIndexed() { index, cachedRate ->
            val found = newRates?.find { currencyRate -> cachedRate.name.equals(currencyRate.name) }
            found?.let { mutatedRates.add(index, it) }
        }
        return mutatedRates
    }

    var hasFocus: Boolean = false
        set(value) {
            field = value
        }
    var rateChangeView: RateChangeView? = null
        set(rateChangeView) {
            field = rateChangeView
        }
    var newValue: Double = 1.0

    override fun getItemCount(): Int {
        return rates?.size ?: 0
    }

    override fun onBindViewHolder(ratesViewHolder: RatesViewHolder, position: Int) {
        rates?.let {
            ratesViewHolder.bind(it.get(position), newValue, rateChangeView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RatesViewHolder {
        val inflatedView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.currency_item_layout, viewGroup, false)
        return RatesViewHolder(inflatedView)
    }

    fun setNewValueAndRate(newValue: Double, currencyRate: CurrencyRate) {
        this.newValue = newValue
        when (hasFocus) {
            false -> {
                val indexOf = rates?.indexOf(currencyRate)
                indexOf?.let {
                    rates?.removeAt(indexOf)
                    rates?.add(0, currencyRate)
                    notifyItemMoved(it, 0)
                }
            }
        }
    }
}