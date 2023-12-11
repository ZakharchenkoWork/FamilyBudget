package com.faigenbloom.famillyspandings.datasources

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        val database = Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "family_budget_database",
        ).fallbackToDestructiveMigration()
            .build()
        val appDatabaseCallback = AppDatabaseCallback(database)
        appDatabaseCallback.onCreate(database.openHelper.writableDatabase)

        database
    }
}
