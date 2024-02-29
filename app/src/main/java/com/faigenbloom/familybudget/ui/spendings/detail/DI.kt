package com.faigenbloom.familybudget.ui.spendings.detail

import com.faigenbloom.familybudget.domain.details.GetAllSpendingDetailsUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val detailsModule = module {
    viewModelOf(::DetailViewModel)
    singleOf(::GetAllSpendingDetailsUseCase)
}
