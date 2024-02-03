package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.datasources.entities.SpendingDetailsCrossRef

class DetailsRepository(
    val dataSource: BaseDataSource,
) {
    suspend fun deleteCrossRef(
        spendingId: String,
        detailId: String,
    ) {
        dataSource.deleteCrossRef(
            SpendingDetailsCrossRef(
                spendingId,
                detailId,
            ),
        )
    }

    suspend fun replaceCrossRef(
        spendingId: String,
        deleteId: String,
        addId: String,
    ) {
        dataSource.deleteCrossRef(
            SpendingDetailsCrossRef(
                spendingId,
                deleteId,
            ),
        )
        dataSource.addCrossRef(
            SpendingDetailsCrossRef(
                spendingId,
                addId,
            ),
        )
    }

    suspend fun getSpendingDetailDuplicate(newDetail: SpendingDetailEntity) =
        dataSource.getSpendingDetailDuplicate(newDetail)

    suspend fun addSpendingDetail(detail: SpendingDetailEntity) =
        dataSource.addSpendingDetail(detail)

    suspend fun getDetailCrossRefs(detailId: String) =
        dataSource.getDetailCrossRefs(detailId)

    suspend fun getSpendingDetails(spendingId: String) =
        dataSource.getSpendingDetails(spendingId)

    suspend fun getAllDetails() =
        dataSource.getAllSpendingDetails()

    suspend fun addNewDetail(
        newDetail: SpendingDetailEntity,
        spendingId: String,
    ) {
        val oldCrossRefs = dataSource.getDetailCrossRefs(newDetail.id)
        if (oldCrossRefs.isEmpty()) {
            dataSource.addSpendingDetail(newDetail)
        }
        dataSource.addCrossRef(
            SpendingDetailsCrossRef(
                spendingId = spendingId,
                detailId = newDetail.id,
            ),
        )
    }

    suspend fun deleteSpendingDetail(id: String) =
        dataSource.deleteSpendingDetail(id)

}
