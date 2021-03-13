package dev.lados.calculator

import android.util.Log
import android.widget.TextView
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.math.sqrt

class Calculator(var display: TextView) : CalculatorInterface {
    private val OPS = "-+/*"
    private var symbols = listOf<String>()
        set(value: List<String>) {

            when (value.isEmpty()) {
                true -> display.text = "0"
                false -> display.text = value.joinToString(" ")
            }

            field = value
        }
    private var isEnteringNumber = false


    override fun clear() {
        isEnteringNumber = false
        symbols = mutableListOf()
    }

    override fun insertOp(token: Char) {
        isEnteringNumber = false
        symbols += token.toString()
    }

    override fun insertDigit(digit: Char) {
        if (!isEnteringNumber) {
            isEnteringNumber = true
            symbols += digit.toString()
            return
        }
        symbols = symbols.dropLast(1) + "${symbols[symbols.lastIndex]}$digit"
    }

    override fun squareRoot() {
        if (symbols.isEmpty()) return
        evaluate()
        val result = beautify(sqrt(symbols[0].toDouble()))
        symbols = mutableListOf(result)
    }

    override fun evaluate() {
        if (symbols.isEmpty()) return
        val s = Stack<Float>()

        symbols = symbols.dropLastWhile { OPS.indexOf(it) !== -1 }

        for (token in postfix()) {
            val idx = OPS.indexOf(token)
            if (idx == -1) {
                s.push(token.toFloat())
            } else {
                val right = s.pop()
                val left = s.pop()
                when (token) {
                    "-" -> s.push(left - right)
                    "+" -> s.push(left + right)
                    "*" -> s.push(left * right)
                    "/" -> s.push(left / right)
                }
            }
        }

        symbols = mutableListOf(beautify(s.pop().toDouble()))
    }

    private fun postfix(): MutableList<String> {
        var parsed = mutableListOf<String>()
        val s = Stack<Int>()

        for (token in symbols) {
            if (token.isEmpty()) continue
            val c = token[0]
            val idx = OPS.indexOf(c)

            if (idx != - 1) {
                if (s.isEmpty()) {
                    s.push(idx)
                }
                else {
                    while (!s.isEmpty()) {
                        val prec2 = s.peek() / 2
                        val prec1 = idx / 2
                        if (prec2 > prec1 || (prec2 == prec1 && c != '^')) {
                            parsed.add("${OPS[s.pop()]}")
                        }
                        else break
                    }
                    s.push(idx)
                }
            }
            else {
                parsed.add(token)
            }
        }

        while (!s.isEmpty()) parsed.add("${OPS[s.pop()]}")
        return parsed
    }

    private fun beautify(num: Double): String {
        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.CEILING
        return df.format(num)
    }
}