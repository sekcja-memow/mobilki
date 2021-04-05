package com.example.maciejladoswt1530.layout

import com.example.maciejladoswt1530.data.CurrencyExchangeRateRecord
import com.github.mikephil.charting.formatter.ValueFormatter

class ExchangeRatesAxisFormatter(private var rates: MutableList<CurrencyExchangeRateRecord>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return rates.getOrNull(value.toInt())?.effectiveDate ?: ""
    }
}