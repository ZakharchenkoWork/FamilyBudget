package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.familybudget.datasources.db.entities.FamilyEntity.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class FamilyEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
) {
    companion object {
        const val TABLE_NAME = "family"
        const val COLUMN_ID = "family_id"
        const val COLUMN_NAME = "name"
    }
}
