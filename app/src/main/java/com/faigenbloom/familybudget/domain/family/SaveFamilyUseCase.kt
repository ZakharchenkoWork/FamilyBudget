package com.faigenbloom.familybudget.domain.family

import com.faigenbloom.familybudget.datasources.entities.FamilyEntity
import com.faigenbloom.familybudget.domain.GenerateIdUseCase
import com.faigenbloom.familybudget.repositories.FamilyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveFamilyUseCase(
    private val repository: FamilyRepository,
    private val generateIdUseCase: GenerateIdUseCase,
) {
    suspend operator fun invoke(
        id: String = "",
        name: String,
    ): String {
        return withContext(Dispatchers.IO) {
            val familyId = generateIdUseCase.invoke(id)
            repository.saveFamily(FamilyEntity(id = familyId, name = name))
            return@withContext familyId
        }
    }
}
