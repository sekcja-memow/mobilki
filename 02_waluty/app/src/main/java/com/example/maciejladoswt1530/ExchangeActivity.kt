package com.example.maciejladoswt1530

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.doOnTextChanged
import com.example.maciejladoswt1530.lib.ExchangeRatesFetcher

class ExchangeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var form: LinearLayout
    private lateinit var errorMessage: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var localCurrencyInput: EditText
    private lateinit var foreignCurrencyInput: EditText
    private lateinit var currencySelector: Spinner
    private var selectedCurrency = 0
    private val fetcher = ExchangeRatesFetcher(this)
    private var changesLocked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.exchange_activity_title)
        setContentView(R.layout.activity_exchange)

        form = findViewById(R.id.form_container)
        errorMessage = findViewById(R.id.error_message)
        progressBar = findViewById(R.id.progress_bar)
        localCurrencyInput = findViewById(R.id.currency_top_value)
        foreignCurrencyInput = findViewById(R.id.currency_bottom_value)
        currencySelector = findViewById(R.id.currency_selector)

        fetcher.onRatesLoad = {
            loadCurrencySelections()
            showForm()

            localCurrencyInput.doOnTextChanged { text, start, count, after ->
                if (text?.isEmpty() == false && !changesLocked) {
                    computeForeignCurrencyValue()
                }
            }

            foreignCurrencyInput.doOnTextChanged { text, start, count, after ->
                if (text?.isEmpty() == false && !changesLocked) {
                    computeLocalCurrencyValue()
                }
            }
        }

        fetcher.onRatesLoadError = {
            showError()
        }

        showLoading()
        fetcher.fetchExchangeRates()
    }

    fun loadCurrencySelections() {
        ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                fetcher.exchangeRates.map { rate -> rate.code }
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            currencySelector.adapter = adapter
            currencySelector.onItemSelectedListener = this
            currencySelector.setSelection(selectedCurrency)
        }
    }

    fun showLoading() {
        form.visibility = View.GONE
        errorMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun showError() {
        form.visibility = View.GONE
        errorMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    fun showForm() {
        form.visibility = View.VISIBLE
        errorMessage.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        selectedCurrency = pos
        computeLocalCurrencyValue()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    fun computeLocalCurrencyValue() {
        val rate = foreignCurrencyInput.text.toString().toFloat() * fetcher.exchangeRates[selectedCurrency].mid
        changesLocked = true
        localCurrencyInput.setText(rate.toString())
        changesLocked = false
    }

    fun computeForeignCurrencyValue() {
        val rate = localCurrencyInput.text.toString().toFloat() / fetcher.exchangeRates[selectedCurrency].mid
        changesLocked = true
        foreignCurrencyInput.setText(rate.toString())
        changesLocked = false
    }
}