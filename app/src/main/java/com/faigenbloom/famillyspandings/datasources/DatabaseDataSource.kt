package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailsCrossRef
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
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

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return appDatabase.categoriesDao().getCategories()
    }

    override suspend fun addCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoriesDao().insert(categoryEntity)
    }

    override suspend fun saveSpending(
        spending: SpendingEntity,
        details: List<SpendingDetailEntity>,
    ) {
        appDatabase.spendingsDao().addSpending(spending)
        appDatabase.spendingsDao().addSpendingDetails(details)
        appDatabase.spendingsDao().addSpendingsCrossRef(
            details.map { detail ->
                SpendingDetailsCrossRef(
                    spendingId = spending.id,
                    detailId = detail.id,
                )
            },
        )
    }

    override suspend fun getSpendings(isPlanned: Boolean): List<SpendingEntity> {
        return appDatabase.spendingsDao().getSpendings(isPlanned)
    }

    override suspend fun getSpending(id: String): SpendingEntity {
        return appDatabase.spendingsDao().getSpending(id)
    }

    override suspend fun getCategoriesSumaries(): List<CategorySummary> {
        val allCategories = getAllCategories().map {
            CategoryData.fromEntity(it)
        }
        val spendings = appDatabase.spendingsDao().getSpendings(false)
        val summaries = ArrayList<CategorySummary>()
        spendings.forEach { spending ->
            summaries.firstOrNull {
                it.id == spending.categoryId
            }?.let {
                it.amount += spending.amount
            } ?: kotlin.run {
                val category = allCategories.first {
                    it.id == spending.categoryId
                }
                summaries.add(
                    CategorySummary(
                        id = spending.categoryId,
                        nameId = category.nameId,
                        name = category.name,
                        iconId = category.iconId,
                        iconUri = category.iconUri,
                        amount = spending.amount,
                    ),
                )
            }
        }
        return summaries
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

    override suspend fun updateCategoryPhoto(id: String, photoUri: String) {
        appDatabase.categoriesDao().updatePhoto(id, photoUri)
    }

    override suspend fun getCategory(id: String): CategoryEntity {
        return appDatabase.categoriesDao().getCategory(id)
    }

    override suspend fun getSpendingDetails(spendingId: String): List<SpendingDetailEntity> {
        val spendingsCrossRef = appDatabase.spendingsDao().getSpendingsCrossRef(spendingId)
        return appDatabase.spendingsDao().getSpendingDetails(spendingsCrossRef.map { it.detailId })
    }
}
