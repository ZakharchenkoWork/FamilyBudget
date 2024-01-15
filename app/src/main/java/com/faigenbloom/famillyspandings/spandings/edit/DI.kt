package com.faigenbloom.famillyspandings.spandings.edit

import com.faigenbloom.famillyspandings.datasources.DatabaseDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val spendingEditModule = module {
    viewModelOf(::SpendingEditViewModel)
    single { SpendingsEditRepository(get<DatabaseDataSource>(), get()) }
}
