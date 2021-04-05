package com.example.myapplication.lib

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.data.CurrencyExchangeRate
import com.example.myapplication.data.ExchangeRatesTable
import com.example.myapplication.fromJson
import com.google.gson.Gson

class ExchangeRatesFetcher(val appContext: Context) {
    var exchangeRates = mutableListOf<CurrencyExchangeRate>()
    var onRatesLoad: (()->Unit)? = null
    var onRatesLoadError: (()->Unit)? = null

    fun fetchExchangeRates() {
        var requestsLeft = 2
        var gotError = false
        val queue = Volley.newRequestQueue(appContext)
        val rates = mutableListOf<CurrencyExchangeRate>()

        fun makeRequest(table: String): JsonArrayRequest {
            return JsonArrayRequest(
                    Request.Method.GET, "https://api.nbp.pl/api/exchangerates/tables/${table}/last/2?format=json", null,
                    { response ->
                        val data = Gson().fromJson<List<ExchangeRatesTable>>(response.toString())
                        val fetchedRates = data[0].rates.zip(data[1].rates) {
                            last, current -> CurrencyExchangeRate(
                                current.currency,
                                current.code,
                                current.mid,
                                last.mid < current.mid,
                                table
                        )
                        }

                        rates.addAll(fetchedRates)

                        requestsLeft--
                        if (requestsLeft == 0 && !gotError) {
                            updateRates(rates)
                        }
                    },
                    { error ->
                        onRatesLoadError?.invoke()
                        gotError = true
                        Log.d("rates:fetchError", error.toString())
                    }
            )
        }

        val tableAReq = makeRequest("a")
        val tableBReq = makeRequest("b")

        queue.add(tableAReq)
        queue.add(tableBReq)
    }


    fun updateRates(rates: List<CurrencyExchangeRate>) {
        exchangeRates.clear()
        exchangeRates.addAll(
                rates.sortedBy { rate -> rate.code }
                        .distinctBy { rate -> rate.code }
        )
        onRatesLoad?.invoke()
    }
}