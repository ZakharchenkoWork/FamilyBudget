package com.faigenbloom.familybudget.datasources.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.familybudget.datasources.entities.BudgetEntity

@Dao
interface BudgetDao {

    @Query("SELECT * FROM ${BudgetEntity.TABLE_NAME}")
    fun getBudget(): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(budgetEntity: BudgetEntity)
}
