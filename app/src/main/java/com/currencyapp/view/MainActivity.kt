package com.currencyapp.view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_LONG
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.currencyapp.R
import com.currencyapp.model.CurrencyData
import com.currencyapp.viewModel.RatesViewModel

class MainActivity : AppCompatActivity() {

    private var ratesModel: RatesViewModel? = null
    private var currencyList: RecyclerView? = null
    private var ratesAdapter: RatesAdapter? = null
    private var hasFocus: Boolean = false
    private var liveData: LiveData<CurrencyData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currencyList = findViewById(R.id.currenycList)
        ratesAdapter = RatesAdapter()
        currencyList?.layoutManager = LinearLayoutManager(this)
        currencyList?.adapter = ratesAdapter
        ratesAdapter?.focusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            run {
                this.hasFocus = hasFocus
            }
        }
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
            Log.d("TEST", "list is ${currencyData?.toString()}")
            currencyData?.let {

                it.rates?.let {
                    ratesAdapter?.setRates(it, hasFocus)
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
}
