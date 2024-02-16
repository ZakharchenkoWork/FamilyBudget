package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.repositories.mappers.CategorySourceMapper
import com.faigenbloom.familybudget.repositories.mappers.FamilySourceMapper
import com.faigenbloom.familybudget.repositories.mappers.PersonSourceMapper
import com.faigenbloom.familybudget.repositories.mappers.SpendingDetailsSourceMapper
import com.faigenbloom.familybudget.repositories.mappers.SpendingSourceMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::DetailsRepository)
    singleOf(::SpendingsRepository)
    singleOf(::CategoriesRepository)
    singleOf(::BudgetPageRepository)
    singleOf(::FamilyRepository)
    singleOf(::AuthRepository)
    singleOf(::SpendingSourceMapper)
    singleOf(::SpendingDetailsSourceMapper)
    singleOf(::FamilySourceMapper)
    singleOf(::PersonSourceMapper)
    singleOf(::CategorySourceMapper)
    singleOf(::SettingsRepository)
}

