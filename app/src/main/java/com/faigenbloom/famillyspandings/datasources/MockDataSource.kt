package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.common.toLongDate
import com.faigenbloom.famillyspandings.common.toLongMoney
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.DateRange
import com.faigenbloom.famillyspandings.datasources.entities.DefaultCategories
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity

import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailsCrossRef
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.ui.spendings.detail.mockSuggestions
import com.faigenbloom.famillyspandings.ui.spendings.list.mockSpendingsWithCategoryList
import java.util.Currency
import java.util.Locale

class MockDataSource : BaseDataSource {
    val categories: ArrayList<CategoryEntity> = ArrayList(
        DefaultCategories.values().map { defaultCategory ->
            CategoryEntity(id = defaultCategory.name, isDefault = true)
        },
    )
    val spendingsEntity: ArrayList<SpendingEntity> = ArrayList(
        mockSpendingsWithCategoryList.map {
            SpendingEntity(
                id = it.id,
                name = it.name,
                amount = it.amount,
                date = it.date.toLongDate(),
                categoryId = it.category.id,
                photoUri = null,
                isPlanned = false,
                isManualTotal = true,
                isHidden = false,
            )
        },
    )

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
        return categories
    }

    override suspend fun getCategoriesByVisibility(isHidden: Boolean): List<CategoryEntity> {
        return categories.filter { it.isHidden == isHidden }
    }


    override suspend fun saveSpending(
        spending: SpendingEntity,
    ) {
        spendingsEntity.add(spending)
    }

    override suspend fun getSpendings(isPlanned: Boolean): List<SpendingEntity> {
        return spendingsEntity
    }

    override suspend fun getSpendingsByDate(
        isPlanned: Boolean,
        from: Long,
        to: Long,
    ): List<SpendingEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpending(id: String): SpendingEntity {
        return spendingsEntity.first { it.id == id }
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

    override suspend fun getChosenCurrency(): Currency {
        return Currency.getInstance(Locale.getDefault())
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

    override suspend fun deleteSpending(id: String) {
        spendingsEntity.removeIf { it.id == id }
    }

    override suspend fun getSpendingsByCategory(id: String): List<SpendingEntity> {
        return spendingsEntity.filter { it.categoryId == id }
    }

    override suspend fun deleteCategory(id: String) {
        categories.removeIf { it.id == id }
    }

    override suspend fun changeCategoryHidden(id: String, isHidden: Boolean) {
        val index = categories.indexOfFirst { it.id == id }
        categories[index] = categories[index].copy(isHidden = isHidden)
    }

    override suspend fun getSpendingsMinMaxDate(planned: Boolean): DateRange {
        TODO("Not yet implemented")
    }

    override suspend fun getSpendingsTotalSpent(planned: Boolean, from: Long, to: Long): Long {
        return 2000
    }

    override suspend fun addCategory(categoryEntity: CategoryEntity) {
        categories.add(categoryEntity)
    }
}
