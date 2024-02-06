package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.common.executeSuspendable
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthNetworkSource(
    private val auth: FirebaseAuth = Firebase.auth,
    private val idSource: IdSource,
) {

    fun isLoggedIn(): Boolean {
        return auth.currentUser?.let {
            idSource[ID.USER] = it.uid
            true
        } ?: false
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        return executeSuspendable { callback ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    idSource[ID.USER] = it.result?.user?.uid ?: ""
                    callback(it.result.user)
                }
        }
    }

    suspend fun login(email: String, password: String): FirebaseUser? {
        return executeSuspendable { callback ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    idSource[ID.USER] = it.result?.user?.uid ?: ""
                    callback(it.result.user)
                }
        }
    }
}
