package com.faigenbloom.familybudget.datasources.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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
        Firebase.storage
    }
    single {
        Firebase.auth
    }
    singleOf(::FamilyNetworkSource)
    singleOf(::AuthNetworkSource)
    singleOf(::SpendingsNetworkSource)
    singleOf(::NetworkDataSource)
    singleOf(::CategoryNetworkSource)
    singleOf(::BudgetNetworkSource)
    singleOf(::ImageSource)
}
