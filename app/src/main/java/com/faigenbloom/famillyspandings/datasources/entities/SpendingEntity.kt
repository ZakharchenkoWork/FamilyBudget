package com.faigenbloom.famillyspandings.datasources.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.famillyspandings.comon.Countable
import com.faigenbloom.famillyspandings.comon.toSortableDate
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class SpendingEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_AMOUNT)
    val amount: Long,
    @ColumnInfo(name = COLUMN_DATE)
    val date: Long,
    @ColumnInfo(name = COLUMN_CATEGORY)
    val categoryId: String,
    @ColumnInfo(name = COLUMN_PHOTO)
    val photoUri: String?,
) : Countable {
    override fun getSortableValue(): Long {
        return amount
    }

    override fun getSortableDate(): Int {
        return date.toSortableDate()
    }

    companion object {
        const val TABLE_NAME = "spendings"
        const val COLUMN_ID = "spending_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHOTO = "photo"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_DATE = "date"
        const val COLUMN_CATEGORY = "category"
    }
}
