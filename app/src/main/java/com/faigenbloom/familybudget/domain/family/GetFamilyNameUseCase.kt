package com.faigenbloom.familybudget.domain.family

import com.faigenbloom.familybudget.repositories.FamilyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFamilyNameUseCase(
    private val repository: FamilyRepository,
) {
    suspend operator fun invoke(familyId: String): String {
        return withContext(Dispatchers.IO) {
            repository.getFamilyName(familyId)
        }
    }
}
