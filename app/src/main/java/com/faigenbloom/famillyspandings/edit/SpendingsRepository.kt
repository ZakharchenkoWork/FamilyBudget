package com.faigenbloom.famillyspandings.edit

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

class SpendingsRepository(private val dataSource: BaseDataSource) {
    suspend fun saveSpending(spending: SpendingEntity) {
        dataSource.saveSpending(spending)
    }

    suspend fun getDetails(spendingId: String) = if (spendingId.isEmpty()) {
        emptyList()
    } else {
        dataSource.getDetails(spendingId)
    }
}
