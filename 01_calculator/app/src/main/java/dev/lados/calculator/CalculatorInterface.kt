package dev.lados.calculator

interface CalculatorInterface {
    fun clear()
    fun insertOp(token: Char)
    fun insertDigit(token: Char)
    fun evaluate()
    fun squareRoot()
}