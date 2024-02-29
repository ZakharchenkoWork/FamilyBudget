package com.faigenbloom.familybudget.domain.mappers

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainMappers = module {
    singleOf(::DetailsMapper)
    singleOf(::SpendingsMapper)
    singleOf(::CategoriesMapper)
    singleOf(::SpendingsWithCategoryMapper)
    singleOf(::PersonMapper)
    singleOf(::FamilyMapper)
    singleOf(::BudgetLineMapper)
    singleOf(::SettingsMapper)
}
