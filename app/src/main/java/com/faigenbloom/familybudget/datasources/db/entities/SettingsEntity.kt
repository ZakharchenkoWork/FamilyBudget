package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.familybudget.datasources.db.entities.SettingsEntity.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class SettingsEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 1L,
    @ColumnInfo(name = COLUMN_CURRENCY)
    val currency: String,
    @ColumnInfo(name = COLUMN_NOTIFICATIONS)
    val isNotificationsEnabled: Boolean,
    @ColumnInfo(name = COLUMN_PASSWORD)
    val isPasswordEnabled: Boolean,
) {
    companion object {
        const val TABLE_NAME = "settings"
        const val COLUMN_ID = "id"
        const val COLUMN_CURRENCY = "currency"
        const val COLUMN_NOTIFICATIONS = "is_notifications_enabled"
        const val COLUMN_PASSWORD = "is_password_enabled"
    }
}
