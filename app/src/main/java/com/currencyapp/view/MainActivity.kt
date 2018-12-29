package com.currencyapp.view

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.currencyapp.R
import com.currencyapp.viewModel.RatesViewModel

class MainActivity : AppCompatActivity() {

    private var ratesModel: RatesViewModel? = null
    private var currencyList: RecyclerView? = null
    private var ratesAdapter: RatesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currencyList = findViewById(R.id.currenycList)
        ratesAdapter = RatesAdapter()
        currencyList?.layoutManager = LinearLayoutManager(this)
        currencyList?.adapter = ratesAdapter
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
        ratesModel?.observe()?.observeForever {
            Log.d("TEST", "list is ${it?.toString()}")
            ratesAdapter?.rates = it
        }
    }

    private fun cancelFetchingData() {
        ratesModel?.cancel()
    }
}
