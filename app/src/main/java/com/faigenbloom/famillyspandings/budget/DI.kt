package com.faigenbloom.famillyspandings.budget

import com.faigenbloom.famillyspandings.datasources.DatabaseDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val budgetPageModule = module {
    viewModelOf(::BudgetPageViewModel)
    singleOf(::DatabaseDataSource)
    single { BudgetPageRepository(get<DatabaseDataSource>()) }
}
