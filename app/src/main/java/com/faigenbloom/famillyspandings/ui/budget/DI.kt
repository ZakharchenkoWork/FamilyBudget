package com.faigenbloom.famillyspandings.ui.budget

import com.faigenbloom.famillyspandings.datasources.DatabaseDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val budgetPageModule = module {
    viewModelOf(::BudgetPageViewModel)

    single { BudgetPageRepository(get<DatabaseDataSource>()) }
}
