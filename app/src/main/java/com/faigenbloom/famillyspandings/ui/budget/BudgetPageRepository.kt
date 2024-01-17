package com.faigenbloom.famillyspandings.ui.budget

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity

class BudgetPageRepository(private val dataSource: BaseDataSource) {
    suspend fun getBudgetData(): BudgetEntity {
        return dataSource.getBudgetData()
    }

    suspend fun saveBudgetData(budget: BudgetEntity) {
        return dataSource.saveBudgetData(budget)
    }
}
