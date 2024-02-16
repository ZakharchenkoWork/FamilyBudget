package com.faigenbloom.familybudget.datasources

import com.faigenbloom.familybudget.datasources.db.entities.BudgetEntity
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.datasources.db.entities.CategoryEntity
import com.faigenbloom.familybudget.datasources.db.entities.DateRange
import com.faigenbloom.familybudget.datasources.db.entities.FamilyEntity
import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity
import com.faigenbloom.familybudget.datasources.db.entities.SettingsEntity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailsCrossRef
import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity
import java.util.Currency

interface BaseDataSource {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(
        name: String,
        familyName: String,
        email: String,
        password: String,
    ): Boolean

    suspend fun getFamily(): FamilyEntity?
    suspend fun getAllCategories(): List<CategoryEntity>
    suspend fun getCategoriesByVisibility(isHidden: Boolean): List<CategoryEntity>
    suspend fun addCategory(categoryEntity: CategoryEntity)
    suspend fun saveDetails(spendingId: String, details: List<SpendingDetailEntity>)
    suspend fun saveSpending(spending: SpendingEntity)
    suspend fun getSpendings(isPlanned: Boolean): List<SpendingEntity>
    suspend fun getSpendingsByDate(isPlanned: Boolean, from: Long, to: Long): List<SpendingEntity>
    suspend fun getSpending(id: String): SpendingEntity
    suspend fun getBudgetData(): BudgetEntity
    suspend fun saveBudgetData(budget: BudgetEntity)
    fun getAllCurrencies(): List<Currency> {
        return Currency.getAvailableCurrencies().toList()
    }
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
    suspend fun getSpendingsMinMaxDate(planned: Boolean): DateRange
    suspend fun getSpendingsTotalSpent(planned: Boolean, from: Long, to: Long): Long
    suspend fun saveFamily(familyEntity: FamilyEntity)
    suspend fun getFamilyMembers(): List<PersonEntity>
    suspend fun saveFamilyMember(member: PersonEntity)
    suspend fun deleteFamilyMember(member: PersonEntity)
    suspend fun saveSpendings(spendings: List<SpendingEntity>)
    suspend fun saveCategories(categories: List<CategoryEntity>)
    suspend fun getBudgetLines(
        isForMonth: Boolean,
        isForFamily: Boolean,
        from: Long,
        to: Long,
    ): List<BudgetLineEntity>

    suspend fun saveBudgetLines(budgetEntities: List<BudgetLineEntity>)
    suspend fun saveSettings(settingsEntity: SettingsEntity)
    suspend fun getSettings(): SettingsEntity?
}
