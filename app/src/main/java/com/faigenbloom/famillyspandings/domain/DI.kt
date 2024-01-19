package com.faigenbloom.famillyspandings.domain

import com.faigenbloom.famillyspandings.domain.categories.DeleteCategoryUseCase
import com.faigenbloom.famillyspandings.domain.spendings.DeleteSpendingUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GenerateIdUseCase)
    singleOf(::NormalizeDateUseCase)
    singleOf(::CalculateTotalUseCase)
    singleOf(::SetPurchasedSpendingUseCase)
    singleOf(::DeleteSpendingUseCase)
    singleOf(::DeleteCategoryUseCase)
}

