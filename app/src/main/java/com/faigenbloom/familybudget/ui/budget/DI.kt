package com.faigenbloom.familybudget.ui.budget

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val budgetPageModule = module {
    viewModelOf(::BudgetPageViewModel)
}
