package com.faigenbloom.famillyspandings.budget

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity

class BadgetPageRepository(private val dataSource: BaseDataSource) {
    suspend fun getBudgetData(): BudgetEntity {
        return dataSource.getBudgetData()
    }
}
