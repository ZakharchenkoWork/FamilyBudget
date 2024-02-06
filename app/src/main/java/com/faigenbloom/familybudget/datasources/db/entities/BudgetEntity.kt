package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.familybudget.datasources.db.entities.BudgetEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class BudgetEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 1L,
    @ColumnInfo(name = COLUMN_FAMILY_TOTAL) val familyTotal: Long,
    @ColumnInfo(name = COLUMN_PERSONAL_TOTAL) val personalTotal: Long,
    @ColumnInfo(name = COLUMN_PLANNED_BUDGET_MONTH) val plannedBudgetMonth: Long,
    @ColumnInfo(name = COLUMN_PLANNED_BUDGET_YEAR) val plannedBudgetYear: Long,
) {
    companion object {
        const val TABLE_NAME = "budget"
        const val COLUMN_ID = "personal_total_month"
        const val COLUMN_FAMILY_TOTAL = "family_total"
        const val COLUMN_PERSONAL_TOTAL = "personal_total"
        const val COLUMN_PLANNED_BUDGET_MONTH = "planned_budget_month"
        const val COLUMN_PLANNED_BUDGET_YEAR = "planned_budget_year"
    }
}
