package com.example.myapplication.layout

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World
import com.example.myapplication.DetailsActivity
import com.example.myapplication.Messages
import com.example.myapplication.R
import com.example.myapplication.data.CurrencyExchangeRate

class CurrencyRateListAdapter(
        private val dataSet: List<CurrencyExchangeRate>,
        private val appContext: Context) :
    RecyclerView.Adapter<CurrencyRateListAdapter.ViewHolder>() {

    val currencies = World.getAllCurrencies()
    val customCurrencyFlags = hashMapOf(
        "EUR" to R.drawable.eu,
        "HKD" to R.drawable.hk,
        "USD" to R.drawable.us
    )
    val specialCurrencyCodes = hashMapOf(
        "IDR" to "MCO",
        "INR" to "IN",
        "BYN" to "BLR",
        "ZWL" to "ZWE",
        "GIP" to "GIB",
        "STN" to "ST",
        "XPF" to "CH",
        "GIP" to "GI",
        "GHS" to "GH",
    )

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val currency_code: TextView
        val currency_rate: TextView
        val currency_flag: ImageView
        val currency_status: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            currency_code = view.findViewById(R.id.currency_code)
            currency_rate = view.findViewById(R.id.currency_rate)
            currency_flag = view.findViewById(R.id.currency_flag)
            currency_status = view.findViewById(R.id.currency_status)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rate_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val rate = dataSet[position]
        val flag = when {
            customCurrencyFlags.containsKey(rate.code) -> customCurrencyFlags[rate.code] ?: 0
            specialCurrencyCodes.containsKey(rate.code) -> World.getFlagOf(World.getCountryFrom(specialCurrencyCodes[rate.code] ?: "").id)
            else -> {
                val currencyMeta = currencies.find { curr -> curr.code.equals(rate.code) }
                World.getFlagOf(World.getCountryFrom(currencyMeta?.country ?: "").id)
            }
        }

        val statusImage = when(rate.wentUp) {
            true -> R.drawable.arrow_green
            false -> R.drawable.arrow_red
        }

        viewHolder.currency_code.text = rate.code
        viewHolder.currency_rate.text = "%.4f".format(rate.mid) + " PLN"
        viewHolder.currency_status.setImageResource(statusImage)
        viewHolder.currency_flag.setImageResource(flag)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(appContext, DetailsActivity::class.java).apply {
                putExtra(Messages.DISPLAYED_CURRENCY_CODE, rate.code)
                putExtra(Messages.DISPLAYED_CURRENCY_NAME, rate.currency)
                putExtra(Messages.DISPLAYED_CURRENCY_TABLE, rate.table)
            }
            if (rate.code !== "") {
                appContext.startActivity(intent)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}