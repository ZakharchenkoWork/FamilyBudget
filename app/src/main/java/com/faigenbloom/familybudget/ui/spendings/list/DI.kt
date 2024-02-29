package com.faigenbloom.familybudget.ui.spendings.list

import com.faigenbloom.familybudget.domain.categories.GetCategoryByIdUseCase
import com.faigenbloom.familybudget.domain.spendings.GetAllSpendingsUseCase
import com.faigenbloom.familybudget.domain.spendings.SortPlatesUseCase
import com.faigenbloom.familybudget.domain.spendings.SpendingsPagingSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val spendingsPageModule = module {
    viewModelOf(::SpendingsListViewModel)
    single { SortPlatesUseCase<SpendingCategoryUiData>() }
    singleOf(::GetCategoryByIdUseCase)
    singleOf(::GetAllSpendingsUseCase)
    singleOf(::SpendingsPagingSource)
}
