package com.faigenbloom.famillyspandings.spandings.edit

import com.faigenbloom.famillyspandings.categories.CategoriesRepository
import com.faigenbloom.famillyspandings.datasources.DatabaseDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val spendingEditModule = module {
    viewModelOf(::SpendingEditViewModel)
    singleOf(::SpendingsRepository)
    single { CategoriesRepository(get<DatabaseDataSource>()) }
    singleOf(::DatabaseDataSource)
}
