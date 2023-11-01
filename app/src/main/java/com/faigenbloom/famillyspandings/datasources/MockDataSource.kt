package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.spandings.SpendingData
import com.faigenbloom.famillyspandings.spandings.edit.Mock
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail

typealias CategoriesMock = com.faigenbloom.famillyspandings.categories.Mock
typealias SpendingsEditMock = Mock
typealias SpendingsShowMock = com.faigenbloom.famillyspandings.spandings.show.Mock
typealias SpendingsPageMock = com.faigenbloom.famillyspandings.spandings.Mock

class MockDataSource : BaseDataSource {
    override suspend fun login(email: String, password: String): Boolean {
        return email == "a" && password == "a"
    }

    override suspend fun register(
        name: String,
        familyName: String,
        email: String,
        password: String,
    ): Boolean {
        return email == "a" && password == "aaaaaaaa"
    }

    override suspend fun getCategories(): List<CategoryData> {
        return CategoriesMock.categoriesList
    }

    override suspend fun saveSpending(spending: SpendingEntity) {}
    override suspend fun getDetails(spendingId: String): List<SpendingDetail> {
        return SpendingsEditMock.mockDetailsList
    }

    override suspend fun getAllSpendings(): List<SpendingData> {
        return SpendingsPageMock.spendingsList
    }

    override suspend fun getSpending(id: String): SpendingEntity {
        return SpendingsShowMock.mockSpendingEntity
    }
}
