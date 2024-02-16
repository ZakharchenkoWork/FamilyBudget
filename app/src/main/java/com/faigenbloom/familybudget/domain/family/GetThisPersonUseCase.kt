package com.faigenbloom.familybudget.domain.family

import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity
import com.faigenbloom.familybudget.repositories.FamilyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetThisPersonUseCase(
    private val familyRepository: FamilyRepository,
    private val idSource: IdSource,
) {
    suspend operator fun invoke(): PersonEntity {
        return withContext(Dispatchers.IO) {
            val personId = idSource[ID.USER]
            familyRepository.getFamilyMembers()
                .first { it.id == personId }
        }
    }
}
