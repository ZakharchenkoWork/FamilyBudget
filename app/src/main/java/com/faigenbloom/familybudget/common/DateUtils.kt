package com.faigenbloom.familybudget.common

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import java.util.UUID

const val ONE_DAY: Long = 24 * 60 * 60 * 1000
const val ONE_MONTH: Long = 31 * ONE_DAY
const val ONE_YEAR: Long = 365 * ONE_DAY
fun LocalDate.toReadableDate(): String {
    return this.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
}

fun LocalDate.toReadableMonth(): String {
    return this.format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault()))
}

fun LocalDate.toReadableYear(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy", Locale.getDefault()))
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

fun String.toSortableDate(): Int {
    return this.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt()
}

fun LocalDate.toLongDate(): Long {
    return this.toEpochDay() * ONE_DAY
}

fun String.toLongDate(): Long {
    return try {
        this.toLocalDate().toEpochDay() * ONE_DAY
    } catch (dtpe: DateTimeParseException) {
        getCurrentDate()
    }
}

fun getCurrentDate(): Long {
    return LocalDate.now().toEpochDay() * ONE_DAY
}

fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault()).toLocalDate()
}

fun String.checkOrGenId() = if (this.isBlank()) {
    UUID.randomUUID().toString()
} else {
    this
}

fun getMonthEndDate(future: Int = 0) = LocalDate.now()
    .toLongDate().getMonthEndDate(future = future)

fun getMonthStartDate(past: Int = 0) = LocalDate.now()
    .toLongDate().getMonthStartDate(past = past)

fun Long.getMonthEndDate(past: Int = 0, future: Int = 0) = toLocalDate()
    .minusMonths(past.toLong())
    .plusMonths(future.toLong()).let {
        LocalDate.of(it.year, it.month.value, it.lengthOfMonth())
    }.toLongDate()

fun Long.getMonthStartDate(past: Int = 0, future: Int = 0) = toLocalDate()
    .minusMonths(past.toLong())
    .plusMonths(future.toLong()).let {
        LocalDate.of(it.year, it.month.value, 1)
    }.toLongDate()

fun getYearStartDate(past: Int = 0) = LocalDate.now().toLongDate()
    .getYearStartDate(past = past)

fun getYearEndDate(future: Int = 0) = LocalDate.now().toLongDate()
    .getYearEndDate(future = future)


fun Long.getYearStartDate(past: Int = 0, future: Int = 0) = toLocalDate()
    .minusYears(past.toLong())
    .plusYears(future.toLong())
    .let {
        LocalDate.of(it.year, 1, 1)
    }.toLongDate()

fun Long.getYearEndDate(past: Int = 0, future: Int = 0) = toLocalDate()
    .minusYears(past.toLong())
    .plusYears(future.toLong())
    .let {
        LocalDate.of(it.year, 12, 31)
    }.toLongDate()
