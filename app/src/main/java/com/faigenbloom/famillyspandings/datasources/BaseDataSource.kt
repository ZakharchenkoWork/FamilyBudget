package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.spandings.SpendingData
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail
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

    suspend fun getCategories(): List<CategoryData>
    suspend fun saveSpending(spending: SpendingEntity)
    suspend fun getDetails(spendingId: String): List<SpendingDetail>
    suspend fun getAllSpendings(): List<SpendingData>
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
}
