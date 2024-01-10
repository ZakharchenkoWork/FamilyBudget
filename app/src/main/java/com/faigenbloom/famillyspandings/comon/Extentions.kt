package com.faigenbloom.famillyspandings.comon

import android.util.Log
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

fun LocalDate.toReadableDate(): String {
    return this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun Long.toReadableDate(): String {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault()).toLocalDate()
        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun Long.toSortableDate(): Int {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault()).toLocalDate()
        .format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt()
}

fun LocalDate.toSortableDate(): Int {
    return this.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt()
}

fun LocalDate.toLongDate(): Long {
    return this.toEpochDay() * 24 * 60 * 60 * 1000
}

fun String.toLongDate(): Long {
    return this.toLocalDate().toEpochDay() * 24 * 60 * 60 * 1000
}

fun getCurrentDate(): Long {
    return LocalDate.now().toEpochDay() * 24 * 60 * 60 * 1000
}

fun String.toLocalDate(): LocalDate {
    try {
        return LocalDate.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    } catch (e: DateTimeParseException) {
        Log.e("Dates", e.localizedMessage ?: "String.toLocalDate failed on $this")
    }
    return LocalDate.now()
}

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault()).toLocalDate()
}

fun String.checkOrGenId() = if (this.isEmpty()) {
    UUID.randomUUID().toString()
} else {
    this
}
