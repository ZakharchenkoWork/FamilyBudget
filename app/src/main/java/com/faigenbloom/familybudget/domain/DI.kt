package com.faigenbloom.familybudget.domain

import com.faigenbloom.familybudget.domain.auth.GetAuthStateUseCase
import com.faigenbloom.familybudget.domain.auth.LoginUserUseCase
import com.faigenbloom.familybudget.domain.auth.RegisterUserUseCase
import com.faigenbloom.familybudget.domain.categories.DeleteCategoryUseCase
import com.faigenbloom.familybudget.domain.family.GetFamilyUseCase
import com.faigenbloom.familybudget.domain.family.SaveFamilyMembersUseCase
import com.faigenbloom.familybudget.domain.family.SaveFamilyUseCase
import com.faigenbloom.familybudget.domain.spendings.DeleteSpendingUseCase
import com.faigenbloom.familybudget.domain.spendings.GetSpentTotalUseCase
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
    singleOf(::GetAuthStateUseCase)
    singleOf(::RegisterUserUseCase)
    singleOf(::LoginUserUseCase)

}

