package com.faigenbloom.familybudget.datasources.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    single {
        FirebaseApp.initializeApp(get())
    }
    single {
        Firebase.firestore
    }
    single {
        Firebase.auth
    }
    singleOf(::FirestoreNetworkSource)
    singleOf(::AuthNetworkSource)
}
