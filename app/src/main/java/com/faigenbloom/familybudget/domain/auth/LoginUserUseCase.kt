package com.faigenbloom.familybudget.domain.auth

import com.faigenbloom.familybudget.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUserUseCase(
    private val authRepository: AuthRepository,
    private val loadAllDataUseCase: LoadAllDataUseCase,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            authRepository.login(email, password)?.let { firebaseUser ->
                loadAllDataUseCase()
            } ?: false
        }
    }
}
