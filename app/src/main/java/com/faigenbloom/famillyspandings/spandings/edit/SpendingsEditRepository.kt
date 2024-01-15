package com.faigenbloom.famillyspandings.spandings.edit

import android.net.Uri
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.getCurrentDate
import com.faigenbloom.famillyspandings.comon.toLongDate
import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailsCrossRef
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.id.IdGenerator

class SpendingsEditRepository(
    private val dataSource: BaseDataSource,
    private val idGenerator: IdGenerator,

    ) {
    suspend fun saveSpending(
        id: String,
        name: String,
        amount: String,
        date: String,
        category: CategoryData,
        photoUri: Uri?,
        details: List<SpendingDetailEntity>,
        isHidden: Boolean,
    ): String {
        val spendingId = idGenerator.checkOrGenId(id)
        val spendingDate = if (date.isBlank()) {
            getCurrentDate()
        } else {
            date.toLongDate()
        }
        compareDetails(spendingId, details)

        dataSource.saveSpending(
            SpendingEntity(
                id = spendingId,
                name = name,
                amount = amount.toLongMoney(),
                date = spendingDate,
                categoryId = idGenerator.checkOrGenId(category.id),
                photoUri = photoUri?.toString(),
                isPlanned = spendingDate > getCurrentDate(),
                isDuplicate = false,
                isHidden = isHidden,
            ),
        )
        return spendingId
    }

    private suspend fun compareDetails(
        spendingId: String,
        details: List<SpendingDetailEntity>,
    ) {
        val oldSpendingDetails = dataSource.getSpendingDetails(spendingId)

        oldSpendingDetails.forEach { oldDetail ->
            val newDetail = details.firstOrNull { newDetail ->
                oldDetail.id == newDetail.id
            }
            val oldDetailCrossRefsList = dataSource.getDetailCrossRefs(oldDetail.id)
            newDetail?.let {
                if (oldDetailCrossRefsList.size == 1) {
                    //it will replace old one
                    dataSource.addSpendingDetail(newDetail)
                } else {
                    dataSource.getSpendingDetailDuplicate(newDetail)?.let { duplicateDetail ->
                        dataSource.deleteCrossRef(
                            SpendingDetailsCrossRef(
                                spendingId,
                                oldDetail.id,
                            ),
                        )
                        dataSource.addCrossRef(
                            SpendingDetailsCrossRef(
                                spendingId,
                                duplicateDetail.id,
                            ),
                        )
                    } ?: kotlin.run {
                        val newDetailNewId = idGenerator.checkOrGenId()
                        dataSource.addSpendingDetail(
                            newDetail.copy(id = newDetailNewId),
                        )
                        dataSource.deleteCrossRef(
                            SpendingDetailsCrossRef(
                                spendingId,
                                oldDetail.id,
                            ),
                        )
                        dataSource.addCrossRef(
                            SpendingDetailsCrossRef(
                                spendingId,
                                newDetailNewId,
                            ),
                        )
                    }
                }

            } ?: kotlin.run {
                if (oldDetailCrossRefsList.size == 1) {
                    dataSource.deleteSpendingDetail(oldDetail.id)
                }
                dataSource.deleteCrossRef(
                    SpendingDetailsCrossRef(
                        spendingId = spendingId,
                        detailId = oldDetail.id,
                    ),
                )
            }
        }

        details.forEach { newDetail ->
            val oldDetail = oldSpendingDetails.firstOrNull { oldDetail ->
                oldDetail.id == newDetail.id
            }
            if (oldDetail == null) {
                val oldCrossRefs = dataSource.getDetailCrossRefs(newDetail.id)
                if (oldCrossRefs.isEmpty()) {
                    dataSource.addSpendingDetail(newDetail)
                }
                dataSource.addCrossRef(
                    SpendingDetailsCrossRef(
                        spendingId = spendingId,
                        detailId = idGenerator.checkOrGenId(newDetail.id),
                    ),
                )
            }
        }
    }

    suspend fun getSpending(id: String): SpendingEntity {
        return dataSource.getSpending(id)
    }

    suspend fun getSpendingDetails(spendingId: String) = if (spendingId.isEmpty()) {
        emptyList()
    } else {
        dataSource.getSpendingDetails(spendingId)
    }
}
