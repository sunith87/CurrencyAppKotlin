package com.currencyapp.model

data class Response(
    val base: String,
    val date: String,
    val rates: Map<String, Float>
)