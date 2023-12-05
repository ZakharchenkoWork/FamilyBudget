package com.faigenbloom.famillyspandings.comon

fun Long.toReadableMoney(): String = ((this.toDouble()) / 100).toString()

fun String.toLongMoney(): Long = if (this.contains(".")) {
    this.replace(".", "").toLong()
} else {
    this.toLong() * 100
}
