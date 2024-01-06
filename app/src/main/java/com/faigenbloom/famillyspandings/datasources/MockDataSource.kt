package com.faigenbloom.famillyspandings.datasources

import android.util.Log
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.DefaultCategories
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.spandings.SpendingData
import com.faigenbloom.famillyspandings.spandings.edit.Mock
import com.faigenbloom.famillyspandings.statistics.CategorySummary

typealias CategoriesMock = com.faigenbloom.famillyspandings.categories.Mock
typealias SpendingsEditMock = Mock
typealias SpendingsShowMock = com.faigenbloom.famillyspandings.spandings.show.Mock
typealias SpendingsPageMock = com.faigenbloom.famillyspandings.spandings.Mock
typealias StatisticsPageMock = com.faigenbloom.famillyspandings.statistics.Mock

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

    override suspend fun getCategories(): List<CategoryEntity> {
        return DefaultCategories.values().map { defaultCategory ->
            CategoryEntity(id = defaultCategory.name, isDefault = true)
        }
    }

    override suspend fun saveSpending(
        spending: SpendingEntity,
        details: List<SpendingDetailEntity>,
    ) {
        Log.i(MockDataSource::class.simpleName, "saveSpending")
    }
    override suspend fun getAllSpendings(): List<SpendingData> {
        return SpendingsPageMock.spendingsList
    }

    override suspend fun getSpending(id: String): SpendingEntity {
        return SpendingEntity(
            id = "",
            name = "",
            amount = 0L,
            date = 0L,
            categoryId = "",
            photoUri = null,
        )
    }

    override suspend fun getCategoriesSumaries(): List<CategorySummary> {
        return StatisticsPageMock.categoriesList
    }

    override suspend fun getBudgetData(): BudgetEntity {
        return BudgetEntity(
            familyTotal = 475500L,
            plannedBudget = 2000000L,
            spent = 1524500L,
            plannedSpendings = 310000L,
        )
    }

    override suspend fun saveBudgetData(budget: BudgetEntity) {
    }

    override suspend fun updateCategoryPhoto(id: String, photoUri: String) {
    }

    override suspend fun getCategory(id: String): CategoryEntity {
        return CategoryEntity("sadf", false)
    }

    override suspend fun getSpendingDetails(spendingId: String): List<SpendingDetailEntity> {
        return emptyList()
    }

    override suspend fun addCategory(categoryEntity: CategoryEntity) {
    }
}
