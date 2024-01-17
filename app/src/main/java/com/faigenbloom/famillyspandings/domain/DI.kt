package com.faigenbloom.famillyspandings.domain

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GenerateIdUseCase)
    singleOf(::NormalizeDateUseCase)
    singleOf(::CalculateTotalUseCase)
    singleOf(::SetPurchasedSpendingUseCase)
}

