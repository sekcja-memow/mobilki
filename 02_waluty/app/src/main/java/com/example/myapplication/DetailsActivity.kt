package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.data.CurrencyExchangeRateHistory
import com.example.myapplication.data.CurrencyExchangeRateRecord
import com.example.myapplication.layout.ExchangeRatesAxisFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson

class DetailsActivity : AppCompatActivity() {
    private lateinit var currencyCode: String
    private lateinit var currencyTable: String
    private lateinit var currentRateView: TextView
    private lateinit var lastRateView: TextView
    private lateinit var weeklyRatesChart: LineChart
    private lateinit var monthlyRatesChart: LineChart

    private var rates = mutableListOf<CurrencyExchangeRateRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(intent.getStringExtra(Messages.DISPLAYED_CURRENCY_NAME)
            ?.split(" ")
            ?.map { word -> word.capitalize() }
            ?.joinToString(" ")
        )
        setContentView(R.layout.activity_details)

        currencyCode = intent.getStringExtra(Messages.DISPLAYED_CURRENCY_CODE) ?: ""
        currencyTable = intent.getStringExtra(Messages.DISPLAYED_CURRENCY_TABLE) ?: ""

        currentRateView = findViewById(R.id.currency_current_rate)
        lastRateView = findViewById(R.id.currency_last_rate)
        weeklyRatesChart = findViewById(R.id.currency_weekly_rates)
        monthlyRatesChart = findViewById(R.id.currency_monthly_rates)

        weeklyRatesChart.legend.isEnabled = false
        weeklyRatesChart.description.isEnabled = false
        weeklyRatesChart.xAxis.labelRotationAngle = -90f
        weeklyRatesChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        weeklyRatesChart.extraBottomOffset = 45f

        monthlyRatesChart.legend.isEnabled = false
        monthlyRatesChart.description.isEnabled = false
        monthlyRatesChart.xAxis.labelRotationAngle = -90f
        monthlyRatesChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        monthlyRatesChart.extraBottomOffset = 45f

        fetchExchangeRates()
    }

    fun fetchExchangeRates() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.nbp.pl/api/exchangerates/rates/${currencyTable}/${currencyCode}/last/30/?format=json"
        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val data = Gson().fromJson<CurrencyExchangeRateHistory>(response.toString())
                rates = data.rates

                currentRateView.text = "${rates[0].mid} PLN"
                lastRateView.text = "${rates[1].mid} PLN"

                val weeklyDataset = rates.slice(0..6).reversed().mapIndexed { idx, rate ->
                    Entry(
                        idx.toFloat(),
                        rate.mid
                    )
                }
                val monthlyDataset = rates.slice(0..29).reversed().mapIndexed { idx, rate ->
                    Entry(
                        idx.toFloat(),
                        rate.mid
                    )
                }

                weeklyRatesChart.data = LineData(LineDataSet(weeklyDataset, ""))
                monthlyRatesChart.data = LineData(LineDataSet(monthlyDataset, ""))

                weeklyRatesChart.xAxis.valueFormatter = ExchangeRatesAxisFormatter(rates)
                monthlyRatesChart.xAxis.valueFormatter = ExchangeRatesAxisFormatter(rates)

                weeklyRatesChart.invalidate()
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
        lastRateView.text = getText(R.string.currency_details_fetching_in_progress)
    }

    fun showFetchingError() {
        currentRateView.text = getText(R.string.currency_details_fetching_error)
        lastRateView.text = getText(R.string.currency_details_fetching_error)
    }
}