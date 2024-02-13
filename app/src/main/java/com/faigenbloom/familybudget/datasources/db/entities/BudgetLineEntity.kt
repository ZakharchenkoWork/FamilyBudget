package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class BudgetLineEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_REPEATABLE_ID)
    val repeatableId: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_AMOUNT)
    val amount: Long,
    @ColumnInfo(name = COLUMN_DATE)
    val sortableDate: Long,
    @ColumnInfo(name = COLUMN_IS_FOR_MONTH)
    val isForMonth: Boolean,
    @ColumnInfo(name = COLUMN_IS_FOR_FAMILY)
    val isForFamily: Boolean,
    @ColumnInfo(name = COLUMN_IS_DEFAULT)
    val isDefault: Boolean = false,
    @ColumnInfo(name = COLUMN_FORMULA)
    val formula: String? = null,
) {
    companion object {
        const val TABLE_NAME = "budget_lines"
        const val COLUMN_ID = "id"
        const val COLUMN_REPEATABLE_ID = "repeatable_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_DATE = "date"
        const val COLUMN_IS_FOR_MONTH = "is_for_month"
        const val COLUMN_IS_FOR_FAMILY = "is_for_family"
        const val COLUMN_IS_DEFAULT = "is_default"
        const val COLUMN_FORMULA = "formula"
    }
}

