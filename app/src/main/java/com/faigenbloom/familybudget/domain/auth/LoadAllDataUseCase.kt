package com.faigenbloom.familybudget.domain.auth

import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.repositories.AuthRepository
import com.faigenbloom.familybudget.repositories.CategoriesRepository
import com.faigenbloom.familybudget.repositories.FamilyRepository
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadAllDataUseCase(
    private val repository: AuthRepository,
    private val familyRepository: FamilyRepository,
    private val spendingsRepository: SpendingsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val idSource: IdSource,
) {
    suspend operator fun invoke(): Boolean {
        return withContext(Dispatchers.IO) {
            val authenticated = repository.isAuthenticated()
            if (authenticated) {
                familyRepository.loadFamily(idSource[ID.USER])
                spendingsRepository.loadSpendings()
                categoriesRepository.loadCategories()
            }
            authenticated
        }
    }
}
