package com.faigenbloom.famillyspandings.budget

import com.faigenbloom.famillyspandings.datasources.BaseDataSource

class BadgetPageRepository(private val dataSource: BaseDataSource) {
    suspend fun getBudgetData(): BudgetData {
        return dataSource.getBudgetData()
    }
}
