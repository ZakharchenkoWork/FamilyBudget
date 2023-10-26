package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.edit.SpendingDetail

typealias CategoriesMock = com.faigenbloom.famillyspandings.categories.Mock
typealias SpendingsEditMock = com.faigenbloom.famillyspandings.edit.Mock

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
}
