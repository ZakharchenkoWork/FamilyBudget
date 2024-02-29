package com.faigenbloom.familybudget.domain.family

import com.faigenbloom.familybudget.domain.mappers.FamilyMapper
import com.faigenbloom.familybudget.domain.mappers.PersonMapper
import com.faigenbloom.familybudget.repositories.FamilyRepository
import com.faigenbloom.familybudget.ui.family.FamilyUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFamilyUseCase(
    private val repository: FamilyRepository,
    private val familyMapper: FamilyMapper,
    private val personMapper: PersonMapper,
) {
    suspend operator fun invoke(): FamilyUiData {
        return withContext(Dispatchers.IO) {
            familyMapper.forUI(repository.loadFamily())
                .copy(
                    members = repository.getFamilyMembers().map {
                        personMapper.forUI(it)
                    },
                )
        }
    }
}
