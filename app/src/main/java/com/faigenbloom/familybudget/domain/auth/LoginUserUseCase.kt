package com.faigenbloom.familybudget.domain.auth

import com.faigenbloom.familybudget.repositories.AuthRepository
import com.faigenbloom.familybudget.repositories.FamilyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUserUseCase(
    private val authRepository: AuthRepository,
    private val familyRepository: FamilyRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            authRepository.login(email, password)?.let { firebaseUser ->
                familyRepository.getFamily(personId = firebaseUser.uid)
                true
            } ?: false
        }
    }
}
