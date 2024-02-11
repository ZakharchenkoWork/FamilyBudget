package com.faigenbloom.familybudget.datasources.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.familybudget.datasources.db.entities.BudgetEntity
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity

@Dao
interface BudgetDao {
    @Query("SELECT * FROM ${BudgetEntity.TABLE_NAME}")
    fun getBudget(): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(budgetEntity: BudgetEntity)

    @Query(
        "SELECT * FROM ${BudgetLineEntity.TABLE_NAME} " +
                "WHERE ${BudgetLineEntity.COLUMN_IS_FOR_MONTH} = :isForMonth " +
                "AND ${BudgetLineEntity.COLUMN_IS_FOR_FAMILY} = :isForFamily",
    )
    fun getBudgetLines(isForMonth: Boolean, isForFamily: Boolean): List<BudgetLineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBudgetLines(defaultBudgetEntities: List<BudgetLineEntity>)
}
