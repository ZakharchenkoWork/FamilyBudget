package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class PersonEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_FAMILY_ID)
    val familyId: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_FAMILY_NAME)
    val familyName: String,
    @ColumnInfo(name = COLUMN_IS_THIS_USER)
    val isThisUser: Boolean,
) {
    companion object {
        const val TABLE_NAME = "person"
        const val COLUMN_ID = "person_id"
        const val COLUMN_FAMILY_ID = "family_id"
        const val COLUMN_FAMILY_NAME = "familyName"
        const val COLUMN_NAME = "name"
        const val COLUMN_IS_THIS_USER = "isThisUser"
    }
}
