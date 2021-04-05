package com.example.maciejladoswt1530.layout

import com.example.maciejladoswt1530.data.GoldExchangeRateRecord
import com.github.mikephil.charting.formatter.ValueFormatter

class GoldRatesAxisFormatter(private var rates: MutableList<GoldExchangeRateRecord>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return rates.getOrNull(value.toInt())?.data ?: ""
    }
}