package com.example.maciejladoswt1530

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.maciejladoswt1530.data.GoldExchangeRateRecord
import com.example.maciejladoswt1530.layout.GoldRatesAxisFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson

class GoldActivity : AppCompatActivity() {
    private lateinit var currentRateView: TextView
    private lateinit var monthlyRatesChart: LineChart

    private var rates = mutableListOf<GoldExchangeRateRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.gold_activity_title)
        setContentView(R.layout.activity_gold)


        currentRateView = findViewById(R.id.currency_current_rate)
        monthlyRatesChart = findViewById(R.id.currency_monthly_rates)

        monthlyRatesChart.legend.isEnabled = false
        monthlyRatesChart.description.isEnabled = false
        monthlyRatesChart.xAxis.labelRotationAngle = -90f
        monthlyRatesChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        monthlyRatesChart.extraBottomOffset = 45f

        fetchExchangeRates()
    }

    fun fetchExchangeRates() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.nbp.pl/api/cenyzlota/last/30?format=json"
        val req = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    val data = Gson().fromJson<MutableList<GoldExchangeRateRecord>>(response.toString())
                    rates = data

                    currentRateView.text = "${rates[0].cena} PLN"

                    val monthlyDataset = rates.slice(0..29).reversed().mapIndexed { idx, rate ->
                        Entry(
                                idx.toFloat(),
                                rate.cena
                        )
                    }

                    monthlyRatesChart.data = LineData(LineDataSet(monthlyDataset, ""))
                    monthlyRatesChart.xAxis.valueFormatter = GoldRatesAxisFormatter(rates)
                    monthlyRatesChart.invalidate()
                },
                { error ->
                    showFetchingError()
                    Log.d("rates:fetchError", error.toString())
                }
        )

        showFetchingProgress()
        queue.add(req)
    }

    fun showFetchingProgress() {
        currentRateView.text = getText(R.string.currency_details_fetching_in_progress)
    }

    fun showFetchingError() {
        currentRateView.text = getText(R.string.currency_details_fetching_error)
    }
}