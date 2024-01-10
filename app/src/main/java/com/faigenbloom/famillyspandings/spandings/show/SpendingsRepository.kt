package com.faigenbloom.famillyspandings.spandings.show

import android.net.Uri
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.checkOrGenId
import com.faigenbloom.famillyspandings.comon.toLongDate
import com.faigenbloom.famillyspandings.comon.toLongMoney
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

    suspend fun duplicateSpending(
        name: String,
        amount: String,
        date: String,
        category: CategoryData,
        photoUri: Uri?,
        isPlanned: Boolean,
        details: List<SpendingDetailEntity>,
    ): String {
        val id = "".checkOrGenId()
        dataSource.saveSpending(
            SpendingEntity(
                id = id,
                name = name,
                amount = amount.toLongMoney(),
                date = date.toLongDate(),
                categoryId = category.id.checkOrGenId(),
                photoUri = photoUri?.toString(),
                isPlanned = isPlanned,
                isDuplicate = true,
            ),
            details = details,
        )
        return id
    }
}
