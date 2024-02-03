package com.faigenbloom.familybudget.domain.family

import com.faigenbloom.familybudget.domain.GenerateIdUseCase
import com.faigenbloom.familybudget.repositories.FamilyRepository
import com.faigenbloom.familybudget.ui.family.PersonMapper
import com.faigenbloom.familybudget.ui.family.PersonUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveFamilyMembersUseCase(
    private val repository: FamilyRepository,
    private val generateIdUseCase: GenerateIdUseCase,
    private val personMapper: PersonMapper,

    ) {
    suspend operator fun invoke(members: List<PersonUiData>) {
        withContext(Dispatchers.IO) {
            repository.saveFamilyMembers(
                members.map {
                    personMapper.forDB(it)
                        .copy(id = generateIdUseCase.invoke(it.id))
                },
            )
        }
    }
}
