// Autor: Maciej Ładoś
//
// Wykonane podpunkty - wszystkie bez:
// - obsługi operatora modulo
// - wyświetlania wyników cząstkowych
// - potencjalnego działania po zmianie symboli reprezentujących operacje
//   (wartości przycisków są zakodowane przez zmienne, natomiast kalkulator
//   wstawia określone, predefiniowane symbole operacji do wyświetlacza)
//
// Przetwarzanie treści kalkulatora jest oparte o algorytm Shunting-Yard, ewaluacja na podstawie prostego stosu.
//
// W przypadku błędnych danych wyświetlana jest wartość "Invalid input", wielokrotne wykorzystanie
// operatora operacji po sobie powoduje ich obcięcie aż do otrzymania pierwszego wyrażenia numerycznego
//
package dev.lados.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var calc: Calculator
    private lateinit var ops: TableLayout
    private lateinit var display: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        ops = findViewById(R.id.ops)
        display = findViewById(R.id.display)
        calc = Calculator(display)

        calc.clear()
        for (button in ops.touchables) {
            button.setOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        try {
            when (view.id) {
                R.id.colon -> {
                    calc.insertDigit('.')
                }
                R.id.one -> {
                    calc.insertDigit('1')
                }
                R.id.two -> {
                    calc.insertDigit('2')
                }
                R.id.three -> {
                    calc.insertDigit('3')
                }
                R.id.four -> {
                    calc.insertDigit('4')
                }
                R.id.five -> {
                    calc.insertDigit('5')
                }
                R.id.six -> {
                    calc.insertDigit('6')
                }
                R.id.seven -> {
                    calc.insertDigit('7')
                }
                R.id.eight -> {
                    calc.insertDigit('8')
                }
                R.id.nine -> {
                    calc.insertDigit('9')
                }
                R.id.add -> {
                    calc.insertOp('+')
                }
                R.id.subtract -> {
                    calc.insertOp('-')
                }
                R.id.product -> {
                    calc.insertOp('*')
                }
                R.id.divide -> {
                    calc.insertOp('/')
                }
                R.id.equal -> {
                    calc.evaluate()
                }
                R.id.sqrt -> {
                    calc.squareRoot()
                }
                R.id.clear -> {
                    calc.clear()
                }
            }
        } catch (e: Exception) {
            calc.clear()
            display.text = "Invalid input"
        }
    }

}