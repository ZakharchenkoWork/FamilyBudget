package com.faigenbloom.familybudget.ui.spendings.edit

import com.faigenbloom.familybudget.domain.details.GetSpendingDetailsByIdUseCase
import com.faigenbloom.familybudget.domain.details.SaveDetailsUseCase
import com.faigenbloom.familybudget.domain.spendings.GetSpendingUseCase
import com.faigenbloom.familybudget.domain.spendings.SaveSpendingUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val spendingEditModule = module {
    viewModelOf(::SpendingEditViewModel)
    singleOf(::SaveSpendingUseCase)
    singleOf(::SaveDetailsUseCase)
    singleOf(::GetSpendingUseCase)
    singleOf(::GetSpendingDetailsByIdUseCase)
}
