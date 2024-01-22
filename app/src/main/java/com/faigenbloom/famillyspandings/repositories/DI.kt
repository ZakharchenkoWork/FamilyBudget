package com.faigenbloom.famillyspandings.repositories

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::DetailsRepository)
    singleOf(::SpendingsRepository)
    singleOf(::CategoriesRepository)
    singleOf(::CurrencyRepository)
}

