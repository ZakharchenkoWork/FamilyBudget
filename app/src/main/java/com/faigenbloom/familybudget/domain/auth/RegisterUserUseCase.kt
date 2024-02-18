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
    private val loginUserUseCase: LoginUserUseCase,
    private val loadAllDataUseCase: LoadAllDataUseCase,
    private val idSource: IdSource,
) {
    suspend operator fun invoke(
        familyId: String,
        name: String,
        familyName: String,
        surname: String,
        email: String,
        password: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            repository.register(email, password)?.let { firebaseUser ->
                idSource[ID.FAMILY] = familyId.ifBlank {
                    saveFamilyUseCase(
                        id = "",
                        name = familyName,
                    )
                }

                saveFamilyMembersUseCase(
                    listOf(
                        PersonUiData(
                            id = firebaseUser.uid,
                            familyName = surname,
                            familyId = idSource[ID.FAMILY],
                            name = name,
                            isThisUser = true,
                        ),
                    ),
                )
                loadAllDataUseCase()
                true
            } ?: kotlin.run {
                loginUserUseCase(email, password)
            }
        }
    }
}
