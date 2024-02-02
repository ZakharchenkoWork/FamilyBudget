package com.faigenbloom.famillyspandings.domain.family

import com.faigenbloom.famillyspandings.domain.GenerateIdUseCase
import com.faigenbloom.famillyspandings.repositories.FamilyPageRepository
import com.faigenbloom.famillyspandings.ui.family.PersonMapper
import com.faigenbloom.famillyspandings.ui.family.PersonUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveFamilyMembersUseCase(
    private val repository: FamilyPageRepository,
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
