/**
 * Maciej_Ładoś_wt_15_30
 *
 * Zostały zrealizowane wszystkie podpunkty z projektu.
 * Wymagane "problematyczne" flagi są podmienione, oraz jeszcze kilka innych których brakowało
 * (nie są to natomiast wszystkie ze względu na ich ilość)
 *
 */
package com.example.maciejladoswt1530

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openRatesActivity(view: View) {
        val intent = Intent(this, RatesActivity::class.java)
        startActivity(intent)
    }

    fun openGoldActivity(view: View) {
        val intent = Intent(this, GoldActivity::class.java)
        startActivity(intent)
    }

    fun openExchangeActivity(view: View) {
        val intent = Intent(this, ExchangeActivity::class.java)
        startActivity(intent)
    }
}