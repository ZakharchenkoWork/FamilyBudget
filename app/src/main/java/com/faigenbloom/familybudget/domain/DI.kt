package com.faigenbloom.familybudget.domain

import com.faigenbloom.familybudget.domain.auth.LoadAllDataUseCase
import com.faigenbloom.familybudget.domain.auth.LoginUserUseCase
import com.faigenbloom.familybudget.domain.auth.RegisterUserUseCase
import com.faigenbloom.familybudget.domain.budget.BudgetLineMapper
import com.faigenbloom.familybudget.domain.budget.CalculateMoneyUseCase
import com.faigenbloom.familybudget.domain.budget.GenerateDefaultBudgetLinesUseCase
import com.faigenbloom.familybudget.domain.budget.GetBudgetLinesUseCase
import com.faigenbloom.familybudget.domain.budget.GetBudgetUseCase
import com.faigenbloom.familybudget.domain.budget.SaveBudgetLinesUseCase
import com.faigenbloom.familybudget.domain.categories.DeleteCategoryUseCase
import com.faigenbloom.familybudget.domain.currency.GetChosenCurrencyUseCase
import com.faigenbloom.familybudget.domain.family.GetFamilyUseCase
import com.faigenbloom.familybudget.domain.family.GetPersonNameUseCase
import com.faigenbloom.familybudget.domain.family.SaveFamilyMembersUseCase
import com.faigenbloom.familybudget.domain.family.SaveFamilyUseCase
import com.faigenbloom.familybudget.domain.spendings.DeleteSpendingUseCase
import com.faigenbloom.familybudget.domain.spendings.GetSpentTotalUseCase
import com.faigenbloom.familybudget.domain.spendings.SetPurchasedSpendingUseCase
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
    singleOf(::LoadAllDataUseCase)
    singleOf(::RegisterUserUseCase)
    singleOf(::LoginUserUseCase)
    singleOf(::GetPersonNameUseCase)
    singleOf(::SaveBudgetLinesUseCase)
    singleOf(::GetBudgetUseCase)
    singleOf(::GetBudgetLinesUseCase)
    singleOf(::GenerateDefaultBudgetLinesUseCase)
    singleOf(::BudgetLineMapper)
    singleOf(::CalculateMoneyUseCase)
}

