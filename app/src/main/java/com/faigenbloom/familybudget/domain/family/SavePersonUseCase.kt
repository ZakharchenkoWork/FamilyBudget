package com.faigenbloom.familybudget.domain.family

import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity
import com.faigenbloom.familybudget.repositories.FamilyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveUserUseCase(
    private val repository: FamilyRepository,
    private val idSource: IdSource,
) {
    suspend operator fun invoke(
        name: String,
        familyName: String,
    ) {
        return withContext(Dispatchers.IO) {
            repository.saveFamilyMember(
                PersonEntity(
                    id = idSource[ID.USER],
                    familyId = idSource[ID.FAMILY],
                    name = name,
                    familyName = familyName,
                    isThisUser = true,
                    isHidden = false,
                ),
            )
        }
    }
}
