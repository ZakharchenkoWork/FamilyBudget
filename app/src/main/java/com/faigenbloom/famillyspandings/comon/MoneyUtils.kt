package com.faigenbloom.famillyspandings.comon

fun Long.toReadableMoney(): String {
    var result = ((this.toDouble()) / 100).toString()
    if (result.contains(".")) {
        if (result.split(".")[1].length == 1) {
            result += "0"
        }
    }
    return result
}

fun String.toLongMoney(): Long = if (this.contains(".")) {
    val parts = this.split(".")
    var result = parts[0]
    result += if (parts[1].length == 1) {
        parts[1] + "0"
    } else {
        parts[1]
    }
    result.toLong()
} else if (isNotBlank()) {
    toLong() * 100L
} else {
    0L
}
