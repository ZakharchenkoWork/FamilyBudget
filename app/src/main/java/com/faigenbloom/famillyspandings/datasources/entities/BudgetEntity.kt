package com.faigenbloom.famillyspandings.datasources.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class BudgetEntity(
    @PrimaryKey
    val id: Long = 1L,
    @ColumnInfo(name = "family_total") val familyTotal: Long,
    @ColumnInfo(name = "planned_budget") val plannedBudget: Long,
    @ColumnInfo(name = "spent") val spent: Long,
    @ColumnInfo(name = "planned_spendings") val plannedSpendings: Long,
) {
    companion object {
        const val TABLE_NAME = "budget"
    }
}
