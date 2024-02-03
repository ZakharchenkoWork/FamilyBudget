package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.entities.SpendingEntity

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

    suspend fun getSpendingsTotalSpent(planned: Boolean, from: Long, to: Long): Long {
        return dataSource.getSpendingsTotalSpent(planned, from, to)
    }
}

