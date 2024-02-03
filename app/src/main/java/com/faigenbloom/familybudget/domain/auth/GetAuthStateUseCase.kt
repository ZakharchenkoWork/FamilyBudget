package com.faigenbloom.familybudget.domain.auth

import com.faigenbloom.familybudget.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAuthStateUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): Boolean {
        return withContext(Dispatchers.IO) {
            repository.isAuthenticated()
        }
    }
}
