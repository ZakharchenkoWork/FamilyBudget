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

    suspend fun getBudgetLines(): List<BudgetLineEntity> {
        return dataSource.getBudgetLines()
    }

}

