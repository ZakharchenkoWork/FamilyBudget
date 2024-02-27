package com.faigenbloom.familybudget.domain.family

import com.faigenbloom.familybudget.repositories.FamilyRepository
import com.faigenbloom.familybudget.ui.family.PersonMapper
import com.faigenbloom.familybudget.ui.family.PersonUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetThisPersonUseCase(
    private val familyRepository: FamilyRepository,
    private val personMapper: PersonMapper,
) {
    suspend operator fun invoke(): PersonUiData {
        return withContext(Dispatchers.IO) {
            personMapper.forUI(familyRepository.getCurrentFamilyMember())
        }
    }
}
