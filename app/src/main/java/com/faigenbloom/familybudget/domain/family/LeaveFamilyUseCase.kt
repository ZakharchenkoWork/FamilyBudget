package com.faigenbloom.familybudget.domain.family

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LeaveFamilyUseCase(
    private val saveFamilyUseCase: SaveFamilyUseCase,
    private val migrateFamilyUseCase: MigrateFamilyUseCase,
) {
    suspend operator fun invoke(familyName: String) {
        return withContext(Dispatchers.IO) {
            val familyId = saveFamilyUseCase(
                id = "",
                name = familyName,
            )
            migrateFamilyUseCase(familyId, false)
        }
    }
}
