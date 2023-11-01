package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.spandings.SpendingData
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail

interface BaseDataSource {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(
        name: String,
        familyName: String,
        email: String,
        password: String,
    ): Boolean

    suspend fun getCategories(): List<CategoryData>
    suspend fun saveSpending(spending: SpendingEntity)
    suspend fun getDetails(spendingId: String): List<SpendingDetail>
    suspend fun getAllSpendings(): List<SpendingData>
    suspend fun getSpending(id: String): SpendingEntity
}
