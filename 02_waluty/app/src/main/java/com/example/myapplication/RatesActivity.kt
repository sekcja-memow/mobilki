package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World
import com.example.myapplication.data.CurrencyExchangeRate
import com.example.myapplication.layout.CurrencyRateListAdapter
import com.example.myapplication.lib.ExchangeRatesFetcher

class RatesActivity : AppCompatActivity() {
    lateinit var listAdapter: CurrencyRateListAdapter
    lateinit var recycler: RecyclerView
    lateinit var progressBar: ProgressBar
    private val fetcher = ExchangeRatesFetcher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        World.init(this);

        setTitle(R.string.rates_title)
        setContentView(R.layout.activity_rates)

        loadRatesList()
    }

    fun loadRatesList() {
        listAdapter = CurrencyRateListAdapter(fetcher.exchangeRates, this)
        recycler = findViewById(R.id.rates)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = listAdapter
        progressBar = findViewById(R.id.progress_bar)

        fetcher.onRatesLoad = {
            listAdapter.notifyDataSetChanged()
            showListData()
        }

        fetcher.onRatesLoadError = {
            val title = getText(R.string.currency_rates_fetching_error).toString()
            fetcher.exchangeRates = mutableListOf(CurrencyExchangeRate(title, "", 0.0f))
            listAdapter.notifyDataSetChanged()
            showListData()
        }

        showListProgress()
        fetcher.fetchExchangeRates()
    }

    fun showListProgress() {
        recycler.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun showListData() {
        recycler.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }


}