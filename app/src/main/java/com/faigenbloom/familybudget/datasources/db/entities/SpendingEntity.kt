package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.familybudget.common.toSortableDate
import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity.Companion.TABLE_NAME
import com.faigenbloom.familybudget.domain.spendings.Countable

@Entity(tableName = TABLE_NAME)
data class SpendingEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_OWNER_ID)
    val ownerId: String,
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
    @ColumnInfo(name = COLUMN_IS_TOTAL_MANUAL)
    val isManualTotal: Boolean,
    @ColumnInfo(name = COLUMN_IS_PLANNED)
    val isPlanned: Boolean,
    @ColumnInfo(name = COLUMN_IS_HIDDEN)
    val isHidden: Boolean,
    @ColumnInfo(name = COLUMN_IS_DUPLICATE)
    val isDuplicate: Boolean = false,
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
        const val COLUMN_OWNER_ID = "owner_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHOTO = "photo"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_DATE = "date"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_IS_TOTAL_MANUAL = "is_total_manual"
        const val COLUMN_IS_PLANNED = "is_planned"
        const val COLUMN_IS_HIDDEN = "is_hidden"
        const val COLUMN_IS_DUPLICATE = "is_duplicate"
    }
}
