package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.REPEAT_INFIX
import com.faigenbloom.familybudget.datasources.db.entities.RepeatableOptionEntity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity
import com.faigenbloom.familybudget.datasources.firebase.NetworkDataSource
import com.faigenbloom.familybudget.repositories.mappers.SpendingDetailsSourceMapper
import com.faigenbloom.familybudget.repositories.mappers.SpendingRepeatableOptionsSourceMapper
import com.faigenbloom.familybudget.repositories.mappers.SpendingSourceMapper

class SpendingsRepository(
    private val networkDataSource: NetworkDataSource,
    private val dataBaseDataSource: BaseDataSource,
    private val spendingSourceMapper: SpendingSourceMapper,
    private val detailsSourceMapper: SpendingDetailsSourceMapper,
    private val repeatablesSourceMapper: SpendingRepeatableOptionsSourceMapper,
    private val idSource: IdSource,
) {
    var lastSpendingDuplicate: SpendingEntity? = null
    suspend fun saveSpending(entity: SpendingEntity) {
        if (entity.isDuplicate) {
            lastSpendingDuplicate = entity
        } else {
            dataBaseDataSource.saveSpending(entity)
            networkDataSource.saveSpending(spendingSourceMapper.forServer(entity))
        }
    }

    suspend fun getSpending(id: String) =
        lastSpendingDuplicate?.let {
            return@let if (it.id == id)
                lastSpendingDuplicate else null
        } ?: dataBaseDataSource.getSpending(id)

    suspend fun getSpendings(isPlanned: Boolean) =
        dataBaseDataSource.getSpendings(isPlanned)

    suspend fun getSpendingsByDate(isPlanned: Boolean, from: Long, to: Long): List<SpendingEntity> {

        return dataBaseDataSource.getSpendingsByDate(isPlanned, from, to)
    }

    suspend fun getSpendingsMinMaxDate(isPlanned: Boolean) =
        dataBaseDataSource.getSpendingsMinMaxDate(isPlanned)


    suspend fun markSpendingPurchased(spendingId: String) {
        val spending = if (spendingId.contains(REPEAT_INFIX)) {
            dataBaseDataSource.getSpending(spendingId.split(REPEAT_INFIX)[1])
        } else{
            dataBaseDataSource.getSpending(spendingId)
        }
        val purchasedSpending = spending.copy(isPlanned = false, repeatOptionsId="")

        if (spending.repeatOptionsId.isNotBlank()){
            val repeatableOptionModel = repeatablesSourceMapper.forServer(dataBaseDataSource.getRepeatableOption(spending.repeatOptionsId))
                .let { it.copy(excludedIDs = it.excludedIDs + spendingId) }


            dataBaseDataSource.saveRepeatable(repeatablesSourceMapper.forDB(repeatableOptionModel))
            networkDataSource.saveRepeatableOptions(repeatableOptionModel)
        }
        dataBaseDataSource.saveSpending(purchasedSpending)
        networkDataSource.saveSpending(spendingSourceMapper.forServer(purchasedSpending))

    }

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
        val loadedRepeatableOptions = networkDataSource.loadRepeatableOptions()
            .map { repeatablesSourceMapper.forDB(it) }
        if (loadedRepeatableOptions.isNotEmpty()) {
            dataBaseDataSource.saveRepeatables(loadedRepeatableOptions)
        }
        dataBaseDataSource.saveSpendings(loadedSpendings.map { spendingSourceMapper.forDB(it) })

        loadedSpendings.forEach { spendingModel ->
            val filteredDetails = loadedDetails.filter { spendingModel.details.contains(it.id) }
            dataBaseDataSource.saveDetails(spendingModel.id, filteredDetails)
        }
    }

    suspend fun saveRepeatableOptions(repeatableOption: RepeatableOptionEntity) {
        dataBaseDataSource.saveRepeatable(repeatableOption)
        networkDataSource.saveRepeatableOptions(repeatablesSourceMapper.forServer(repeatableOption))
    }
    suspend fun getRepeatableOption(repeatableOptionId: String): RepeatableOptionEntity {
        return dataBaseDataSource.getRepeatableOption(repeatableOptionId)
    }

    suspend fun getThisUserSpendings() =
        dataBaseDataSource.getThisUserSpendings(idSource[ID.USER])

    suspend fun migrateSpendings(
        spending: SpendingEntity,
    ) {
        networkDataSource.saveSpending(spendingSourceMapper.forServer(spending))
    }
}

