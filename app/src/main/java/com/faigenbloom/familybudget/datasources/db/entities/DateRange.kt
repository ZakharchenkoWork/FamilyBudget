package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo

data class DateRange(
    @ColumnInfo(name = COLUMN_MAX)
    val max: Long = 0,
    @ColumnInfo(name = COLUMN_MIN)
    val min: Long = 0,
) {
    companion object {
        const val COLUMN_MAX = "max"
        const val COLUMN_MIN = "min"
    }
}
