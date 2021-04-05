package com.example.myapplication.layout

import com.example.myapplication.data.CurrencyExchangeRateRecord
import com.example.myapplication.data.GoldExchangeRateRecord
import com.github.mikephil.charting.formatter.ValueFormatter

class GoldRatesAxisFormatter(private var rates: MutableList<GoldExchangeRateRecord>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return rates.getOrNull(value.toInt())?.data ?: ""
    }
}