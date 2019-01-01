package com.currencyapp.model

data class CurrencyRate(
    val name: String,
    val rate: Double
) {
    override fun equals(other: Any?): Boolean {
        if (other is CurrencyRate){
            if (other.name.equals(this.name)) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        return 32 + name.hashCode()
    }
}
