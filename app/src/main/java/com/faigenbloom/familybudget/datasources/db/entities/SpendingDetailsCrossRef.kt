package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailsCrossRef.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [SpendingEntity.COLUMN_ID, SpendingDetailEntity.COLUMN_ID],
)
data class SpendingDetailsCrossRef(
    @ColumnInfo(name = SpendingEntity.COLUMN_ID) val spendingId: String,
    @ColumnInfo(name = SpendingDetailEntity.COLUMN_ID) val detailId: String,
) {
    companion object {
        const val TABLE_NAME = "spending_details_cross_ref"
    }
}
