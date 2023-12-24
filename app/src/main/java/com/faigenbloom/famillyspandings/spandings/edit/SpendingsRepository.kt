package com.faigenbloom.famillyspandings.spandings.edit

import android.net.Uri
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.checkOrGenId
import com.faigenbloom.famillyspandings.comon.toLongDate
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
    ) {
        dataSource.saveSpending(
            SpendingEntity(
                id = id.checkOrGenId(),
                name = name,
                amount = amount.toDatabaseLong(),
                date = date.toLongDate(),
                categoryId = category.id.checkOrGenId(),
                photoUri = photoUri?.toString(),
            ),
            details = details,
        )
    }

    suspend fun getDetails(spendingId: String) = if (spendingId.isEmpty()) {
        emptyList()
    } else {
        dataSource.getDetails(spendingId)
    }
}
