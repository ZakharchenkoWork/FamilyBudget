package com.faigenbloom.famillyspandings.common

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
        parts[1].subSequence(0, 2)
    }
    result.toLong()
} else if (isNotBlank()) {
    toLong() * 100L
} else {
    0L
}

fun String.toNormalizedMoney(): String {
    return if (this.isBlank()) {
        "0.00"
    } else {
        val result = this.dropWhile {
            it == '0'
        }
        if (this.contains(".").not()) {
            "$result.00"
        } else {
            val parts = result.split(".")
            val start = parts[0].ifBlank {
                "0"
            }
            if (parts[1].isBlank()) {
                "$start.00"
            } else if (parts[1].length == 1) {
                "$start.${parts[1]}0"
            } else {
                "$start.${parts[1].subSequence(0, 2)}"
            }
        }
    }
}
