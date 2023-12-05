package com.faigenbloom.famillyspandings.datasources

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "family_budget_database",
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
