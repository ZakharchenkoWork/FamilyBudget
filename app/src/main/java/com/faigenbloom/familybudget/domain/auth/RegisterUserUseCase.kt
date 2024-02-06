package com.faigenbloom.familybudget.domain.auth

import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.domain.family.SaveFamilyMembersUseCase
import com.faigenbloom.familybudget.domain.family.SaveFamilyUseCase
import com.faigenbloom.familybudget.repositories.AuthRepository
import com.faigenbloom.familybudget.ui.family.PersonUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterUserUseCase(
    private val repository: AuthRepository,
    private val saveFamilyUseCase: SaveFamilyUseCase,
    private val saveFamilyMembersUseCase: SaveFamilyMembersUseCase,
    private val idSource: IdSource,
) {
    suspend operator fun invoke(
        name: String,
        familyName: String,
        email: String,
        password: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            repository.register(email, password)?.let { firebaseUser ->
                val familyId = saveFamilyUseCase(name = familyName)
                idSource[ID.FAMILY] = familyId
                saveFamilyMembersUseCase(
                    listOf(
                        PersonUiData(
                            id = firebaseUser.uid,
                            familyName = familyName,
                            familyId = familyId,
                            name = name,
                            isThisUser = true,
                        ),
                    ),
                )
                true
            } ?: false
        }
    }
}
