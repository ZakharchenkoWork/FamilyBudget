package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class SpendingDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_AMOUNT)
    val amount: Long,
    @ColumnInfo(name = COLUMN_BARCODE)
    val barcode: String = "",
) {
    companion object {
        const val TABLE_NAME = "spending_detail"
        const val COLUMN_ID = "spending_detail_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_BARCODE = "barcode"
    }
}
