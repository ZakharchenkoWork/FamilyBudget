package com.faigenbloom.familybudget.datasources

import android.util.Log
import com.faigenbloom.familybudget.datasources.db.entities.BudgetEntity
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.datasources.db.entities.CategoryEntity
import com.faigenbloom.familybudget.datasources.db.entities.DateRange
import com.faigenbloom.familybudget.datasources.db.entities.FamilyEntity
import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailsCrossRef
import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity
import java.util.Currency
import java.util.Locale
import java.util.UUID

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
        return true
    }

    override suspend fun getFamily(): FamilyEntity {
        return appDatabase.familyDao().getFamily()
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

    override suspend fun saveDetails(spendingId: String, details: List<SpendingDetailEntity>) {
        val oldSpendingDetails = getSpendingDetails(spendingId)
        oldSpendingDetails.forEach { oldDetail ->
            val newDetail = details.firstOrNull { newDetail ->
                oldDetail.id == newDetail.id
            }

            val oldDetailCrossRefsList = getDetailCrossRefs(oldDetail.id)
            newDetail?.let {
                if (oldDetailCrossRefsList.size == 1) {
                    //it will replace old one
                    addSpendingDetail(newDetail)
                } else {
                    getSpendingDetailDuplicate(newDetail)
                        ?.let { duplicateDetail ->
                            replaceCrossRef(
                                spendingId,
                                oldDetail.id,
                                duplicateDetail.id,
                            )
                        } ?: kotlin.run {
                        val newDetailNewId = UUID.randomUUID().toString()
                        addSpendingDetail(
                            newDetail.copy(newDetailNewId),
                        )
                        replaceCrossRef(
                            spendingId,
                            oldDetail.id,
                            newDetailNewId,
                        )
                    }
                }
            } ?: kotlin.run {
                if (oldDetailCrossRefsList.size == 1) {
                    deleteSpendingDetail(oldDetail.id)
                }
                deleteCrossRef(
                    SpendingDetailsCrossRef(
                        spendingId = spendingId,
                        detailId = oldDetail.id,
                    ),
                )
            }
        }


        details.forEach {
            Log.d("ID", it.id)
            val newDetail = it.copy(it.id.ifBlank { UUID.randomUUID().toString() })
            oldSpendingDetails.firstOrNull { oldDetail ->
                oldDetail.id == newDetail.id
            } ?: kotlin.run {
                addNewDetail(newDetail, spendingId)
            }
        }
    }

    private suspend fun addNewDetail(
        newDetail: SpendingDetailEntity,
        spendingId: String,
    ) {
        val oldCrossRefs = getDetailCrossRefs(newDetail.id)
        if (oldCrossRefs.isEmpty()) {
            addSpendingDetail(newDetail)
        }
        addCrossRef(
            SpendingDetailsCrossRef(
                spendingId = spendingId,
                detailId = newDetail.id,
            ),
        )
    }

    private suspend fun replaceCrossRef(
        spendingId: String,
        deleteId: String,
        addId: String,
    ) {
        deleteCrossRef(
            SpendingDetailsCrossRef(
                spendingId,
                deleteId,
            ),
        )
        addCrossRef(
            SpendingDetailsCrossRef(
                spendingId,
                addId,
            ),
        )
    }

    override suspend fun saveSpending(
        spending: SpendingEntity,
    ) {
        appDatabase.spendingsDao().addSpending(spending)
    }

    override suspend fun getSpendings(isPlanned: Boolean): List<SpendingEntity> {
        return appDatabase.spendingsDao().getSpendings(isPlanned)
    }

    override suspend fun getSpendingsByDate(
        isPlanned: Boolean,
        from: Long,
        to: Long,
    ): List<SpendingEntity> {
        return appDatabase.spendingsDao().getSpendingsByDate(isPlanned, from, to)
    }

    override suspend fun getSpending(id: String): SpendingEntity {
        return appDatabase.spendingsDao().getSpending(id)
    }

    override suspend fun getBudgetData(): BudgetEntity {
        return appDatabase.budgetDao().getBudget() ?: BudgetEntity(
            familyTotal = 0L,
            id = 0L,
            personalTotal = 0L,
            plannedBudgetMonth = 0L,
            plannedBudgetYear = 0L,
        )
    }

    override suspend fun saveBudgetData(budget: BudgetEntity) {
        appDatabase.budgetDao().update(budget)
    }

    override suspend fun getChosenCurrency(): Currency {
        return Currency.getInstance(Locale.getDefault())
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

    override suspend fun deleteCrossRef(spendingDetailsCrossRef: SpendingDetailsCrossRef) {
        return appDatabase.spendingsDao().deleteCrossRef(
            spendingDetailsCrossRef.spendingId,
            spendingDetailsCrossRef.detailId,
        )
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

    override suspend fun getSpendingsMinMaxDate(planned: Boolean): DateRange {
        return appDatabase.spendingsDao().getSpendingsMinMaxDate(planned)
    }

    override suspend fun getSpendingsTotalSpent(planned: Boolean, from: Long, to: Long): Long {
        return appDatabase.spendingsDao().getSpendingsTotalSpent(planned, from, to)
    }

    override suspend fun saveFamily(familyEntity: FamilyEntity) {
        appDatabase.familyDao().saveFamily(familyEntity)
    }

    override suspend fun getFamilyMembers(): List<PersonEntity> {
        return appDatabase.familyDao().getFamilyMembers()
    }

    override suspend fun saveFamilyMember(member: PersonEntity) {
        appDatabase.familyDao().saveFamilyMember(member)
    }

    override suspend fun deleteFamilyMember(member: PersonEntity) {
        //TODO: Add delete functionality
    }

    override suspend fun saveSpendings(spendings: List<SpendingEntity>) {
        appDatabase.spendingsDao().saveSpendings(spendings)
    }

    override suspend fun saveCategories(categories: List<CategoryEntity>) {
        appDatabase.categoriesDao().saveCategories(categories)
    }

    override suspend fun getBudgetLines(
        isForMonth: Boolean,
        isForFamily: Boolean,
    ): List<BudgetLineEntity> {
        return appDatabase.budgetDao().getBudgetLines(isForMonth, isForFamily)
    }

    override suspend fun saveBudgetLines(defaultBudgetEntities: List<BudgetLineEntity>) {
        appDatabase.budgetDao().addBudgetLines(defaultBudgetEntities)
    }
}
