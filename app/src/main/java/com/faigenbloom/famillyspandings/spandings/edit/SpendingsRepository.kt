package com.faigenbloom.famillyspandings.spandings.edit

import android.net.Uri
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.checkOrGenId
import com.faigenbloom.famillyspandings.comon.getCurrentDate
import com.faigenbloom.famillyspandings.comon.toLongDate
import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

class SpendingsRepository(private val dataSource: BaseDataSource) {
    suspend fun saveSpending(
        id: String,
        name: String,
        amount: String,
        date: String,
        category: CategoryData,
        photoUri: Uri?,
        details: List<SpendingDetailEntity>,
    ): String {
        val spendingId = id.checkOrGenId()
        val spendingDate = if (date.isBlank()) {
            getCurrentDate()
        } else {
            date.toLongDate()
        }
        dataSource.saveSpending(
            SpendingEntity(
                id = spendingId,
                name = name,
                amount = amount.toLongMoney(),
                date = spendingDate,
                categoryId = category.id.checkOrGenId(),
                photoUri = photoUri?.toString(),
                isPlanned = spendingDate > getCurrentDate(),
                isDuplicate = false,
            ),
            details = details,
        )
        return spendingId
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
