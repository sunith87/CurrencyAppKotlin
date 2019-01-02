package com.currencyapp.view

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_LONG
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager
import com.currencyapp.R
import com.currencyapp.model.CurrencyData
import com.currencyapp.model.CurrencyRate
import com.currencyapp.viewModel.RatesViewModel

class MainActivity : AppCompatActivity(), RateChangeView {

    private var ratesModel: RatesViewModel? = null
    private var currencyList: RecyclerView? = null
    private var ratesAdapter: RatesAdapter? = null
    private var liveData: LiveData<CurrencyData>? = null
    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currencyList = findViewById(R.id.currenycList)
        ratesAdapter = RatesAdapter()
        currencyList?.layoutManager = LinearLayoutManager(this)
        currencyList?.adapter = ratesAdapter
        ratesAdapter?.rateChangeView = this
        ratesModel = ViewModelProviders.of(this).get(RatesViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        startFetchingData()
    }

    override fun onPause() {
        super.onPause()
        cancelFetchingData()
    }

    private fun startFetchingData() {
        liveData?.removeObservers(this)
        liveData = ratesModel?.observe()
        liveData?.observe(this, Observer { currencyData ->
            currencyData?.let {

                it.rates?.let {
                    ratesAdapter?.rates = it.toMutableList()
                }

                it.error?.let {
                    showError(it)
                }
            }
        })
    }


    private fun showError(error: Throwable) {
        error.message?.let {
            Snackbar.make(findViewById(android.R.id.content), "Error caused by $it", LENGTH_LONG).show()
        }
    }

    private fun cancelFetchingData() {
        ratesModel?.cancel()
    }

    override fun hideKeyboard() {
        val inputMethodManager: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currencyList?.clearFocus()
    }

    override fun setFocus(focus: Boolean) {
        ratesAdapter?.hasFocus = focus
    }

    override fun newCurrencyInputValue(newValue: Double, currencyRate: CurrencyRate) {
        currencyList?.clearFocus()
        ratesAdapter?.setNewValueAndRate(newValue, currencyRate)
        handler.post({ currencyList?.layoutManager?.scrollToPosition(0) })

    }
}
