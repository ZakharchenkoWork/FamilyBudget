package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.statistics.CategorySummary
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
    suspend fun addCategory(categoryEntity: CategoryEntity)

    suspend fun saveSpending(spending: SpendingEntity, details: List<SpendingDetailEntity>)
    suspend fun getSpendings(isPlanned: Boolean): List<SpendingEntity>
    suspend fun getSpending(id: String): SpendingEntity
    suspend fun getCategoriesSumaries(): List<CategorySummary>
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
}
