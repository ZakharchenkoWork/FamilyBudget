package com.faigenbloom.familybudget.domain.family

import com.faigenbloom.familybudget.repositories.FamilyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPersonNameUseCase(
    private val familyRepository: FamilyRepository,
) {
    suspend operator fun invoke(personId: String): String {
        return withContext(Dispatchers.IO) {
            familyRepository.getFamilyMembers()
                .first { it.id == personId }.name
        }
    }
}
