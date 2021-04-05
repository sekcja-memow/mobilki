package com.example.myapplication.data

data class CurrencyExchangeRate(
    val currency: String,
    val code: String,
    val mid: Float,
    val wentUp: Boolean = false,
    val table: String = "a"
)