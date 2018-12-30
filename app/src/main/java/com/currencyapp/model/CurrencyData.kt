package com.currencyapp.model

class CurrencyData private constructor(val rates: List<CurrencyRate>?, val error: Throwable?) {

    companion object {
        fun error(error: Throwable): CurrencyData {
            return CurrencyData(null, error)
        }

        fun data(rates: List<CurrencyRate>): CurrencyData {
            return CurrencyData(rates, null)
        }
    }
}
