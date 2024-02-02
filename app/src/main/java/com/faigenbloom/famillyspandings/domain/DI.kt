package com.faigenbloom.famillyspandings.domain

import com.faigenbloom.famillyspandings.domain.categories.DeleteCategoryUseCase
import com.faigenbloom.famillyspandings.domain.family.GetFamilyUseCase
import com.faigenbloom.famillyspandings.domain.family.SaveFamilyMembersUseCase
import com.faigenbloom.famillyspandings.domain.family.SaveFamilyUseCase
import com.faigenbloom.famillyspandings.domain.spendings.DeleteSpendingUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetSpentTotalUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GenerateIdUseCase)
    singleOf(::NormalizeDateUseCase)
    singleOf(::CalculateTotalUseCase)
    singleOf(::SetPurchasedSpendingUseCase)
    singleOf(::DeleteSpendingUseCase)
    singleOf(::DeleteCategoryUseCase)
    singleOf(::GetChosenCurrencyUseCase)
    singleOf(::GetSpentTotalUseCase)
    singleOf(::GetFamilyUseCase)
    singleOf(::SaveFamilyUseCase)
    singleOf(::SaveFamilyMembersUseCase)

}

