package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.spandings.SpendingData
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail
import com.faigenbloom.famillyspandings.statistics.CategorySummary

class DatabaseDataSource(val appDatabase: AppDatabase) : BaseDataSource {
    override suspend fun login(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        name: String,
        familyName: String,
        email: String,
        password: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getCategories(): List<CategoryEntity> {
        return appDatabase.categoriesDao().getCategories()
    }

    override suspend fun saveSpending(spending: SpendingEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getDetails(spendingId: String): List<SpendingDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllSpendings(): List<SpendingData> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpending(id: String): SpendingEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getCategoriesSumaries(): List<CategorySummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getBudgetData(): BudgetEntity {
        return appDatabase.budgetDao().getBudget() ?: BudgetEntity(
            familyTotal = 0L,
            plannedBudget = 0L,
            spent = 0L,
            plannedSpendings = 0L,
        )
    }

    override suspend fun saveBudgetData(budget: BudgetEntity) {
        appDatabase.budgetDao().update(budget)
    }
}
