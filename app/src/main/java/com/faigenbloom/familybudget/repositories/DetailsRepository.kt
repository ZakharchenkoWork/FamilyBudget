package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailsCrossRef
import com.faigenbloom.familybudget.datasources.firebase.NetworkDataSource
import com.faigenbloom.familybudget.repositories.mappers.SpendingDetailsSourceMapper

class DetailsRepository(
    private val dataBaseDataSource: BaseDataSource,
    private val networkDataSource: NetworkDataSource,
    private val spendingDetailsSourceMapper: SpendingDetailsSourceMapper,
) {
    var spendingDuplicateId: String? = null
    var lastSpendingDetailsDuplicate: List<SpendingDetailEntity> = emptyList()
    suspend fun deleteCrossRef(
        spendingId: String,
        detailId: String,
    ) {
        dataBaseDataSource.deleteCrossRef(
            SpendingDetailsCrossRef(
                spendingId,
                detailId,
            ),
        )
    }

    suspend fun getDetailCrossRefs(detailId: String) =
        dataBaseDataSource.getDetailCrossRefs(detailId)

    suspend fun getSpendingDetails(spendingId: String) =
        if (spendingId == spendingDuplicateId) {
            lastSpendingDetailsDuplicate
        } else dataBaseDataSource.getSpendingDetails(spendingId)

    suspend fun getAllDetails() =
        dataBaseDataSource.getAllSpendingDetails()

    suspend fun deleteSpendingDetail(id: String) =
        dataBaseDataSource.deleteSpendingDetail(id)

    fun saveSpendingDetailsForDuplicate(
        spendingId: String,
        details: List<SpendingDetailEntity>,
    ) {
        spendingDuplicateId = spendingId
        lastSpendingDetailsDuplicate = details
    }

    suspend fun saveSpendingDetails(spendingId: String, details: List<SpendingDetailEntity>) {
        dataBaseDataSource.saveDetails(spendingId, details)
        val savedDetails = dataBaseDataSource.getSpendingDetails(spendingId)
        networkDataSource.saveDetails(
            spendingId,
            savedDetails.map { spendingDetailsSourceMapper.forServer(it) },
        )
    }

    suspend fun migrateSpendingDetails(spendingId: String, details: List<SpendingDetailEntity>) {
        networkDataSource.saveDetails(
            spendingId,
            details.map { spendingDetailsSourceMapper.forServer(it) },
        )
    }
}
