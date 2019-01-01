package com.currencyapp.view

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
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
    TextView.OnEditorActionListener, TextWatcher {

    private var hasFocus: Boolean = false
    private var handler:Handler = Handler()

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        rateChangeView?.setFocus(hasFocus)
    }

    override fun onEditorAction(editText: TextView?, id: Int, event: KeyEvent?): Boolean {
        if ((event?.getAction() == KeyEvent.ACTION_DOWN)
        ) {
            rateChangeView?.hideKeyboard()
            return true
        }

        return false
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d("TEST", "afterTextChanged - ${s.toString()}")
        if (hasFocus) {
            val newValue = s.toString().toDouble() / currencyRate.rate
            Log.d("TEST", "afterTextChanged newValue - $newValue")
            rateChangeView?.newCurrencyInputValue(newValue, currencyRate)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d("TEST", "beforeTextChanged - ${s.toString()}")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("TEST", "onTextChanged - ${s.toString()}")
    }

}