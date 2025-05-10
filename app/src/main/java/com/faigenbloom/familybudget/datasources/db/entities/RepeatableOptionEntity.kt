package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.familybudget.datasources.db.entities.RepeatableOptionEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RepeatableOptionEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
     @ColumnInfo(name = COLUMN_START_DATE)
    val startDate: Long,
    @ColumnInfo(name = COLUMN_END_DATE)
    val endDate: Long,
    @ColumnInfo(name = COLUMN_REPEATABLE_TYPE)
    val repeatType: RepeatOptions,
    @ColumnInfo(name = COLUMN_EXCLUDED_IDS)
    val excludedIDs: String
) {
    companion object {
        const val TABLE_NAME = "repeatables"
        const val COLUMN_ID = "id"
        const val COLUMN_START_DATE = "start"
        const val COLUMN_END_DATE = "end"
        const val COLUMN_REPEATABLE_TYPE = "type"
        const val COLUMN_EXCLUDED_IDS = "excluded"
    }
}
