package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.db.entities.BudgetEntity
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity

class BudgetPageRepository(private val dataSource: BaseDataSource) {
    suspend fun getBudgetData(): BudgetEntity {
        return dataSource.getBudgetData()
    }

    suspend fun saveBudgetData(budget: BudgetEntity) {
        dataSource.saveBudgetData(budget)
    }

    suspend fun getBudgetLines(
        isForMonth: Boolean,
        isForFamily: Boolean,
    ): List<BudgetLineEntity> {
        return dataSource.getBudgetLines(isForMonth, isForFamily)
    }

    suspend fun saveBudgetLines(defaultBudgetEntities: List<BudgetLineEntity>) {
        dataSource.saveBudgetLines(defaultBudgetEntities)
    }
}

