package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailsCrossRef
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import java.util.Currency
import java.util.Locale

interface BaseDataSource {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(
        name: String,
        familyName: String,
        email: String,
        password: String,
    ): Boolean

    suspend fun getAllCategories(): List<CategoryEntity>
    suspend fun getCategoriesByVisibility(isHidden: Boolean): List<CategoryEntity>
    suspend fun addCategory(categoryEntity: CategoryEntity)

    suspend fun saveSpending(spending: SpendingEntity)
    suspend fun getSpendings(isPlanned: Boolean): List<SpendingEntity>
    suspend fun getSpending(id: String): SpendingEntity
    suspend fun getBudgetData(): BudgetEntity
    suspend fun saveBudgetData(budget: BudgetEntity)
    fun getAllCurrencies(): List<Currency> {
        return Currency.getAvailableCurrencies().toList()
    }

    fun getChoosenCurrency(): Currency {
        return Currency.getInstance(Locale.getDefault())
    }

    suspend fun updateCategoryPhoto(id: String, photoUri: String)
    suspend fun getCategory(id: String): CategoryEntity
    suspend fun getSpendingDetails(spendingId: String): List<SpendingDetailEntity>
    suspend fun getSpendingDetailDuplicate(entity: SpendingDetailEntity): SpendingDetailEntity?
    suspend fun getAllSpendingDetails(): List<SpendingDetailEntity>
    suspend fun updatePlanned(spendingId: String, isPlanned: Boolean)
    suspend fun addSpendingDetail(spendingDetailEntity: SpendingDetailEntity)
    suspend fun addCrossRef(crossRef: SpendingDetailsCrossRef)
    suspend fun getDetailCrossRefs(detailId: String): List<SpendingDetailsCrossRef>
    suspend fun deleteCrossRef(spendingDetailsCrossRef: SpendingDetailsCrossRef)
    suspend fun deleteSpendingDetail(id: String)
    suspend fun deleteSpending(id: String)
    suspend fun getSpendingsByCategory(id: String): List<SpendingEntity>
    suspend fun deleteCategory(id: String)
    suspend fun changeCategoryHidden(id: String, isHidden: Boolean)
}
