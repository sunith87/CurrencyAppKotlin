package com.currencyapp.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.currencyapp.data.RateProvider
import com.currencyapp.model.CurrencyRate
import kotlinx.coroutines.*

class RatesViewModel(val app: Application) : AndroidViewModel(app) {

    companion object {
        private val DELAY = 1000L
    }

    val responseLiveData: MutableLiveData<List<CurrencyRate>> = MutableLiveData()
    val rateProvider: RateProvider = RateProvider()
    private var observeJob: Job? = null

    fun observe(): LiveData<List<CurrencyRate>> {
        if (observeJob != null) {
            cancel()
        }
        observeJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                doFetch()
                delay(DELAY)
            }
        }
        return responseLiveData
    }

    private suspend fun doFetch() {
        val responseData = rateProvider.fetch()
        val list: List<CurrencyRate> = responseData.rates.map { entry -> CurrencyRate(entry.key, entry.value) }
        responseLiveData.postValue(list)
    }

    fun cancel() {
        rateProvider.cancel()
        observeJob?.cancel()
    }
}