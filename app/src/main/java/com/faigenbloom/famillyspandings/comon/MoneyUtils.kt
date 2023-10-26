package com.faigenbloom.famillyspandings.comon

fun Long.toReadableMoney(): String {
    val value: Double = (this.toDouble()) / 100
    return value.toString()
    // String.format("%.2f", value)
}
