package com.faigenbloom.famillyspandings.budget

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.MockDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val budgetPageModule = module {
    viewModelOf(::BudgetPageViewModel)
    singleOf(::BadgetPageRepository)
    singleOf(::MockDataSource) bind BaseDataSource::class
}
