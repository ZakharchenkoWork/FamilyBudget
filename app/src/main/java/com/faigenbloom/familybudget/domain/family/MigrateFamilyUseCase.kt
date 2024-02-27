package com.faigenbloom.familybudget.domain.family

import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.domain.auth.LoadAllDataUseCase
import com.faigenbloom.familybudget.repositories.CategoriesRepository
import com.faigenbloom.familybudget.repositories.DetailsRepository
import com.faigenbloom.familybudget.repositories.FamilyRepository
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MigrateFamilyUseCase(
    private val repository: FamilyRepository,
    private val categoriesRepository: CategoriesRepository,
    private val spendingsRepository: SpendingsRepository,
    private val detailsRepository: DetailsRepository,
    private val loadAllDataUseCase: LoadAllDataUseCase,
    private val idSource: IdSource,
) {
    suspend operator fun invoke(familyId: String, isHideUserSpending: Boolean): Result {
        return withContext(Dispatchers.IO) {
            if (familyId == idSource[ID.FAMILY]) {
                return@withContext Result.SameFamily
            } else if (repository.getFamilyName(familyId).isBlank()) {
                return@withContext Result.NoSuchFamily
            }
            val oldFamilyId = idSource[ID.FAMILY]
            idSource[ID.FAMILY] = familyId
            repository.migrateFamilyMember(repository.getCurrentFamilyMember(), oldFamilyId)
            val allThisUserSpendings = spendingsRepository.getThisUserSpendings()
            allThisUserSpendings.forEach {
                val spending = if (isHideUserSpending) {
                    it.copy(isHidden = true)
                } else it
                spendingsRepository.migrateSpendings(spending)
                val spendingDetails = detailsRepository.getSpendingDetails(spending.id)
                detailsRepository.migrateSpendingDetails(spending.id, spendingDetails)
            }
            categoriesRepository.getCategories(true)
                .filter { it.isDefault.not() }
                .forEach {
                    categoriesRepository.migrateCategory(it)
                }

            repository.deleteUserFromFamily()
            loadAllDataUseCase()
            return@withContext Result.Success
        }
    }
}

sealed class Result {
    data object Success : Result()
    data object SameFamily : Result()
    data object NoSuchFamily : Result()
}
