package com.example.myapplication.data

data class CurrencyExchangeRateHistory (
    val table: String,
    val currency: String,
    val code: String,
    val rates: MutableList<CurrencyExchangeRateRecord>
)