package com.currencyapp.viewModel

import android.app.Application
import android.arch.lifecycle.*
import com.currencyapp.data.RateProvider
import com.currencyapp.model.CurrencyData
import com.currencyapp.model.CurrencyRate
import kotlinx.coroutines.*

class RatesViewModel(val app: Application) : AndroidViewModel(app) {

    companion object {
        private val DELAY = 1000L
    }

    val responseLiveData: MutableLiveData<CurrencyData> = MutableLiveData()
    val rateProvider: RateProvider = RateProvider()
    private var observeJob: Job? = null

    fun observe(): LiveData<CurrencyData> {
        startFetch()
        return responseLiveData
    }

    private fun startFetch() {
        observeJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                doFetch()
                delay(DELAY)
            }
        }

        observeJob?.invokeOnCompletion { _ -> observeJob = null }
    }

    private fun doFetch() {
        val responseData = rateProvider.fetch()
        if (responseData.rates.isNotEmpty()) {
            val list: List<CurrencyRate> = responseData.rates.map { entry -> CurrencyRate(entry.key, entry.value) }
            responseLiveData.postValue(CurrencyData.data(list))
        } else {
            responseData.error?.let {
                responseLiveData.postValue(CurrencyData.error(it))
            }
        }
    }

    fun cancel() {
        observeJob?.cancel()
    }
}