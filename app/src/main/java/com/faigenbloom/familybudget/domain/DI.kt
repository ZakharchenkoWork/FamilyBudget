package com.faigenbloom.familybudget.domain

import com.faigenbloom.familybudget.domain.auth.LoadAllDataUseCase
import com.faigenbloom.familybudget.domain.auth.LoginUserUseCase
import com.faigenbloom.familybudget.domain.auth.RegisterUserUseCase
import com.faigenbloom.familybudget.domain.budget.BudgetLineMapper
import com.faigenbloom.familybudget.domain.budget.CalculateFormulasUseCase
import com.faigenbloom.familybudget.domain.budget.CalculateMoneyUseCase
import com.faigenbloom.familybudget.domain.budget.GenerateDefaultBudgetLinesUseCase
import com.faigenbloom.familybudget.domain.budget.GetBudgetLinesUseCase
import com.faigenbloom.familybudget.domain.budget.ReCalculateFormulasUseCase
import com.faigenbloom.familybudget.domain.budget.SaveBudgetLinesUseCase
import com.faigenbloom.familybudget.domain.categories.DeleteCategoryUseCase
import com.faigenbloom.familybudget.domain.currency.GetAllCurrenciesUseCase
import com.faigenbloom.familybudget.domain.currency.GetChosenCurrencyUseCase
import com.faigenbloom.familybudget.domain.currency.GetSettingsUseCase
import com.faigenbloom.familybudget.domain.currency.SaveSettingsUseCase
import com.faigenbloom.familybudget.domain.family.GetFamilyLinkUseCase
import com.faigenbloom.familybudget.domain.family.GetFamilyNameUseCase
import com.faigenbloom.familybudget.domain.family.GetFamilyUseCase
import com.faigenbloom.familybudget.domain.family.GetPersonNameUseCase
import com.faigenbloom.familybudget.domain.family.GetThisPersonUseCase
import com.faigenbloom.familybudget.domain.family.LeaveFamilyUseCase
import com.faigenbloom.familybudget.domain.family.MigrateFamilyUseCase
import com.faigenbloom.familybudget.domain.family.SaveFamilyMembersUseCase
import com.faigenbloom.familybudget.domain.family.SaveFamilyUseCase
import com.faigenbloom.familybudget.domain.family.SaveUserUseCase
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
    singleOf(::SaveSettingsUseCase)
    singleOf(::GetAllCurrenciesUseCase)
    singleOf(::GetSpentTotalUseCase)
    singleOf(::GetFamilyUseCase)
    singleOf(::GetFamilyNameUseCase)
    singleOf(::SaveFamilyUseCase)
    singleOf(::SaveFamilyMembersUseCase)
    singleOf(::LoadAllDataUseCase)
    singleOf(::RegisterUserUseCase)
    singleOf(::LoginUserUseCase)
    singleOf(::GetPersonNameUseCase)
    singleOf(::SaveBudgetLinesUseCase)
    singleOf(::GetBudgetLinesUseCase)
    singleOf(::GenerateDefaultBudgetLinesUseCase)
    singleOf(::GetFamilyLinkUseCase)
    singleOf(::BudgetLineMapper)
    singleOf(::CalculateMoneyUseCase)
    singleOf(::CalculateFormulasUseCase)
    singleOf(::ReCalculateFormulasUseCase)
    singleOf(::MigrateFamilyUseCase)
    singleOf(::LeaveFamilyUseCase)
    singleOf(::GetThisPersonUseCase)
    singleOf(::GetSettingsUseCase)
    singleOf(::SaveUserUseCase)
}


