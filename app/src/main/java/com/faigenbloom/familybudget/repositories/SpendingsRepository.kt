package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity
import com.faigenbloom.familybudget.datasources.firebase.NetworkDataSource
import com.faigenbloom.familybudget.repositories.mappers.SpendingDetailsSourceMapper
import com.faigenbloom.familybudget.repositories.mappers.SpendingSourceMapper

class SpendingsRepository(
    private val networkDataSource: NetworkDataSource,
    private val dataBaseDataSource: BaseDataSource,
    private val spendingSourceMapper: SpendingSourceMapper,
    private val detailsSourceMapper: SpendingDetailsSourceMapper,
    private val idSource: IdSource,
) {
    var lastSpendingDuplicate: SpendingEntity? = null
    suspend fun saveSpending(entity: SpendingEntity) {
        val spendingEntity = if (entity.ownerId.isBlank())
            entity.copy(ownerId = idSource[ID.USER])
        else
            entity

        if (spendingEntity.isDuplicate) {
            lastSpendingDuplicate = spendingEntity
        } else {
            dataBaseDataSource.saveSpending(spendingEntity)
            networkDataSource.saveSpending(spendingSourceMapper.forServer(spendingEntity))
        }
    }

    suspend fun getSpending(id: String) =
        lastSpendingDuplicate?.let {
            return@let if (it.id == id)
                lastSpendingDuplicate else null
        } ?: dataBaseDataSource.getSpending(id)

    suspend fun getSpendings(isPlanned: Boolean) =
        dataBaseDataSource.getSpendings(isPlanned)

    suspend fun getSpendingsByDate(isPlanned: Boolean, from: Long, to: Long) =
        dataBaseDataSource.getSpendingsByDate(isPlanned, from, to)

    suspend fun getSpendingsMinMaxDate(isPlanned: Boolean) =
        dataBaseDataSource.getSpendingsMinMaxDate(isPlanned)


    suspend fun markSpendingPurchased(spendingId: String) =
        dataBaseDataSource.updatePlanned(spendingId, false)

    suspend fun deleteSpending(spendingId: String) {
        dataBaseDataSource.deleteSpending(spendingId)
    }

    suspend fun getSpendingsTotalSpent(planned: Boolean, from: Long, to: Long): Long {
        return dataBaseDataSource.getSpendingsTotalSpent(planned, from, to)
    }

    suspend fun loadSpendings() {
        val loadedSpendings = networkDataSource.loadSpendings()
        val loadedDetails = networkDataSource.loadDetails()
            .map { detailsSourceMapper.forDB(it) }

        dataBaseDataSource.saveSpendings(loadedSpendings.map { spendingSourceMapper.forDB(it) })

        loadedSpendings.forEach { spendingModel ->
            val filteredDetails = loadedDetails.filter { spendingModel.details.contains(it.id) }
            dataBaseDataSource.saveDetails(spendingModel.id, filteredDetails)
        }
    }
}

