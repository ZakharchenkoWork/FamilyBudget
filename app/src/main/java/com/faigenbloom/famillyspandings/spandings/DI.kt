package com.faigenbloom.famillyspandings.spandings

import com.faigenbloom.famillyspandings.datasources.DatabaseDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val spendingsPageModule = module {
    viewModelOf(::SpendingsPageViewModel)
    single { SpendingsPageRepository(get<DatabaseDataSource>()) }
}
