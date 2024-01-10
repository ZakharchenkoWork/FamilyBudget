package com.faigenbloom.famillyspandings.spandings.show

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

class SpendingsRepository(private val dataSource: BaseDataSource) {
    suspend fun getSpending(id: String): SpendingEntity {
        return dataSource.getSpending(id)
    }

    suspend fun getCategory(id: String): CategoryEntity {
        return dataSource.getCategory(id)
    }

    suspend fun getSpendingDetails(spendingId: String): List<SpendingDetailEntity> {
        return dataSource.getSpendingDetails(spendingId)
    }

    suspend fun markSpendingPurchased(spendingId: String) {
        dataSource.updatePlanned(spendingId, false)
    }
}
