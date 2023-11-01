package com.faigenbloom.famillyspandings.comon

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toReadable(): String {
    return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(this)
}

fun LocalDate.toSortableDate(): Int {
    return DateTimeFormatter.ofPattern("yyyyMMdd").format(this).toInt()
}

fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}
