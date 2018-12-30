package com.currencyapp.model

data class Response(
    val base: String,
    val date: String,
    val rates: Map<String, Float>,
    val error: Throwable? = null
) {
    constructor(
        error: Throwable
    ) : this("", "", mutableMapOf(), error)
}