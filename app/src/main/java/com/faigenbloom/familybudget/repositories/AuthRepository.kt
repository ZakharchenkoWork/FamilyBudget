package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.firebase.AuthNetworkSource
import com.google.firebase.auth.FirebaseUser

class AuthRepository(
    private val authNetworkSource: AuthNetworkSource,
) {
    suspend fun isAuthenticated(): Boolean {
        return authNetworkSource.isLoggedIn()
    }

    suspend fun register(
        email: String,
        password: String,
    ): FirebaseUser? {
        return authNetworkSource.register(email, password)
    }

    suspend fun login(
        email: String,
        password: String,
    ): FirebaseUser? {
        return authNetworkSource.login(email, password)
    }
}
