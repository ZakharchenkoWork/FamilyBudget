package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.comon.toLongDate
import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.DefaultCategories
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailsCrossRef
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.spandings.edit.detail.mockSuggestions
import com.faigenbloom.famillyspandings.spandings.mockSpendingsList

import com.faigenbloom.famillyspandings.statistics.CategorySummary
import com.faigenbloom.famillyspandings.statistics.mockCategoriesSummaryList

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

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return DefaultCategories.values().map { defaultCategory ->
            CategoryEntity(id = defaultCategory.name, isDefault = true)
        }
    }

    override suspend fun saveSpending(
        spending: SpendingEntity,
    ) {

    }

    override suspend fun getSpendings(isPlanned: Boolean): List<SpendingEntity> {
        return mockSpendingsList.map {
            SpendingEntity(
                id = it.id,
                name = it.name,
                amount = it.amount,
                date = it.date.toLongDate(),
                categoryId = it.category.id,
                photoUri = null,
                isPlanned = false,
                isHidden = false,
            )
        }
    }

    override suspend fun getSpending(id: String): SpendingEntity {
        return SpendingEntity(
            id = "",
            name = "",
            amount = 0L,
            date = 0L,
            categoryId = "",
            photoUri = null,
            isPlanned = false,
            isHidden = false,
        )
    }

    override suspend fun getCategoriesSumaries(): List<CategorySummary> {
        return mockCategoriesSummaryList
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

    override suspend fun getSpendingDetailDuplicate(entity: SpendingDetailEntity): SpendingDetailEntity {
        return SpendingDetailEntity(
            id = "2",
            name = "3",
            amount = 4L,
        )
    }

    override suspend fun getAllSpendingDetails(): List<SpendingDetailEntity> {
        return mockSuggestions.map {
            SpendingDetailEntity(
                id = it.id,
                name = it.name,
                amount = it.amount.toLongMoney(),
            )
        }
    }

    override suspend fun updatePlanned(spendingId: String, isPlanned: Boolean) {

    }

    override suspend fun addSpendingDetail(spendingDetailEntity: SpendingDetailEntity) {

    }

    override suspend fun addCrossRef(crossRef: SpendingDetailsCrossRef) {

    }

    override suspend fun getDetailCrossRefs(detailId: String): List<SpendingDetailsCrossRef> {
        return emptyList()
    }

    override suspend fun deleteCrossRef(spendingDetailsCrossRef: SpendingDetailsCrossRef) {

    }

    override suspend fun deleteSpendingDetail(id: String) {

    }

    override suspend fun addCategory(categoryEntity: CategoryEntity) {
    }
}
