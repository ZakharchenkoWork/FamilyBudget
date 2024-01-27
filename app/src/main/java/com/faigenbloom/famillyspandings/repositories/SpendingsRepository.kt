package com.faigenbloom.famillyspandings.repositories

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

class SpendingsRepository(
    private val dataSource: BaseDataSource,
) {
    suspend fun saveSpending(entity: SpendingEntity) = dataSource.saveSpending(entity)
    suspend fun getSpending(id: String) = dataSource.getSpending(id)

    suspend fun getSpendings(isPlanned: Boolean) =
        dataSource.getSpendings(isPlanned)

    suspend fun getSpendingsByDate(isPlanned: Boolean, from: Long, to: Long) =
        dataSource.getSpendingsByDate(isPlanned, from, to)

    suspend fun getSpendingsMinMaxDate(isPlanned: Boolean) =
        dataSource.getSpendingsMinMaxDate(isPlanned)


    suspend fun markSpendingPurchased(spendingId: String) =
        dataSource.updatePlanned(spendingId, false)

    suspend fun deleteSpending(spendingId: String) {
        dataSource.deleteSpending(spendingId)
    }
}

