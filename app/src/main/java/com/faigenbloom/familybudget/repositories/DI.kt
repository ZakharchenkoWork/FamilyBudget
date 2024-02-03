package com.faigenbloom.familybudget.repositories

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::DetailsRepository)
    singleOf(::SpendingsRepository)
    singleOf(::CategoriesRepository)
    singleOf(::CurrencyRepository)
    singleOf(::BudgetPageRepository)
    singleOf(::FamilyRepository)
    singleOf(::AuthRepository)
}

