package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.db.entities.BudgetEntity
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity

class BudgetPageRepository(private val dataSource: BaseDataSource) {
    suspend fun getBudgetData(): BudgetEntity {
        return dataSource.getBudgetData()
    }

    suspend fun getBudgetLines(
        isForMonth: Boolean,
        isForFamily: Boolean,
        from: Long,
        to: Long,
    ): List<BudgetLineEntity> {
        return dataSource.getBudgetLines(
            isForMonth,
            isForFamily,
            from,
            to,
        )
    }

    suspend fun saveBudgetLines(budgetEntities: List<BudgetLineEntity>) {
        dataSource.saveBudgetLines(budgetEntities)
    }
}

