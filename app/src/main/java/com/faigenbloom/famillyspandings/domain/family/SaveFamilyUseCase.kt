package com.faigenbloom.famillyspandings.domain.family

import com.faigenbloom.famillyspandings.datasources.entities.FamilyEntity
import com.faigenbloom.famillyspandings.domain.GenerateIdUseCase
import com.faigenbloom.famillyspandings.repositories.FamilyPageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveFamilyUseCase(
    private val repository: FamilyPageRepository,
    private val generateIdUseCase: GenerateIdUseCase,
) {
    suspend operator fun invoke(id: String = "", name: String): String {
        return withContext(Dispatchers.IO) {
            val familyId = generateIdUseCase.invoke(id)
            repository.saveFamily(FamilyEntity(id = familyId, name = name))
            return@withContext familyId
        }
    }
}
