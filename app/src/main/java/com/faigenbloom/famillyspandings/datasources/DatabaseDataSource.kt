package com.faigenbloom.famillyspandings.datasources

import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailsCrossRef
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

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

    override suspend fun getCategoriesByVisibility(isHidden: Boolean): List<CategoryEntity> {
        return appDatabase.categoriesDao().getCategoriesByVisibility(isHidden)
    }

    override suspend fun addCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoriesDao().insert(categoryEntity)
    }

    override suspend fun saveSpending(
        spending: SpendingEntity,
    ) {
        appDatabase.spendingsDao().addSpending(spending)
    }

    override suspend fun getSpendings(isPlanned: Boolean): List<SpendingEntity> {
        return appDatabase.spendingsDao().getSpendings(isPlanned)
    }

    override suspend fun getSpending(id: String): SpendingEntity {
        return appDatabase.spendingsDao().getSpending(id)
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

    override suspend fun getSpendingDetailDuplicate(entity: SpendingDetailEntity): SpendingDetailEntity? {
        return appDatabase.spendingsDao().getSpendingDetailDuplicate(entity.name, entity.amount)
    }

    override suspend fun getAllSpendingDetails(): List<SpendingDetailEntity> {
        return appDatabase.spendingsDao().getAllSpendingDetails()
    }

    override suspend fun updatePlanned(spendingId: String, isPlanned: Boolean) {
        appDatabase.spendingsDao().updatePlanned(spendingId, isPlanned)
    }

    override suspend fun addSpendingDetail(spendingDetailEntity: SpendingDetailEntity) {
        appDatabase.spendingsDao().addSpendingDetail(spendingDetailEntity)
    }

    override suspend fun addCrossRef(crossRef: SpendingDetailsCrossRef) {
        appDatabase.spendingsDao().addSpendingCrossRef(crossRef)
    }

    override suspend fun getDetailCrossRefs(detailId: String): List<SpendingDetailsCrossRef> {
        return appDatabase.spendingsDao().getDetailCrossRefs(detailId)
    }

    override suspend fun deleteCrossRef(crossRef: SpendingDetailsCrossRef) {
        return appDatabase.spendingsDao().deleteCrossRef(crossRef.spendingId, crossRef.detailId)
    }

    override suspend fun deleteSpendingDetail(id: String) {
        appDatabase.spendingsDao().deleteSpendingDetail(id)
    }

    override suspend fun deleteSpending(id: String) {
        appDatabase.spendingsDao().deleteSpending(id)
    }

    override suspend fun getSpendingsByCategory(id: String): List<SpendingEntity> {
        return appDatabase.spendingsDao().getSpendingsByCategory(id)
    }

    override suspend fun deleteCategory(id: String) {
        appDatabase.categoriesDao().deleteCategory(id)
    }

    override suspend fun changeCategoryHidden(id: String, isHidden: Boolean) {
        appDatabase.categoriesDao().hideCategory(id, isHidden)
    }
}
