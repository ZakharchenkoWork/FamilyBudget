package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.common.executeSuspendable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthNetworkSource(
    private val auth: FirebaseAuth = Firebase.auth,
) {

    suspend fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        return executeSuspendable { callback ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    callback(it.result.user)
                }
        }
    }

    suspend fun login(email: String, password: String): FirebaseUser? {
        return executeSuspendable { callback ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    callback(it.result.user)
                }
        }
    }
}
