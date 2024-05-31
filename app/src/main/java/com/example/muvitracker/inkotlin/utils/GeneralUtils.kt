package com.example.muvitracker.inkotlin.utils


fun Double.toString1stDecimalApproximation(): String {
    return String.format("%.1f", this)
}
