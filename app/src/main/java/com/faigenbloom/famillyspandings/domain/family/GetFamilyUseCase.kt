package com.faigenbloom.famillyspandings.domain.family

import com.faigenbloom.famillyspandings.repositories.FamilyPageRepository
import com.faigenbloom.famillyspandings.ui.family.FamilyMapper
import com.faigenbloom.famillyspandings.ui.family.FamilyUiData
import com.faigenbloom.famillyspandings.ui.family.PersonMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFamilyUseCase(
    private val repository: FamilyPageRepository,
    private val familyMapper: FamilyMapper,
    private val personMapper: PersonMapper,
) {
    suspend operator fun invoke(): FamilyUiData {
        return withContext(Dispatchers.IO) {
            familyMapper.forUI(repository.getFamily())
                .copy(
                    members = repository.getFamilyMembers().map {
                        personMapper.forUI(it)
                    },
                )
        }
    }
}
