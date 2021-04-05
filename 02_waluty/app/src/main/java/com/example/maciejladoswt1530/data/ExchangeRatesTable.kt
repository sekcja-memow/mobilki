package com.example.maciejladoswt1530.data

data class ExchangeRatesTable(
    val table: Char,
    val no: String,
    val effectiveDate: String,
    val rates: List<CurrencyExchangeRate>
)