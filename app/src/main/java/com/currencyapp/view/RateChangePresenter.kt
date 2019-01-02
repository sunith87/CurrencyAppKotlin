package com.currencyapp.view

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.currencyapp.model.CurrencyRate

interface RateChangeView {
    fun setFocus(focus: Boolean)
    fun hideKeyboard()
    fun newCurrencyInputValue(newValue: Double, currencyRate: CurrencyRate)
}

class RateChangePresenter(
    val rateChangeView: RateChangeView?,
    val currencyRate: CurrencyRate
) :
    View.OnFocusChangeListener,
    TextWatcher {

    private var hasFocus: Boolean = false
    private var handler: Handler = Handler()
    private var newValue: Double? = null
    private var runnable: Runnable = Runnable {
        newValue?.let {
            Log.d("TEST", "afterTextChanged newValue - $newValue")
            rateChangeView?.newCurrencyInputValue(it, currencyRate)
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        rateChangeView?.setFocus(hasFocus)
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d("TEST", "afterTextChanged - ${s.toString()}")
        if (hasFocus) {
            newValue = s.toString().toDouble() / currencyRate.rate
            handler.postDelayed(runnable, 1000)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d("TEST", "beforeTextChanged - ${s.toString()}")
        handler.removeCallbacks(runnable)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("TEST", "onTextChanged - ${s.toString()}")
    }

}