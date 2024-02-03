package com.faigenbloom.familybudget.ui.spendings.detail

import com.faigenbloom.familybudget.domain.details.GetAllSpendingDetailsUseCase
import com.faigenbloom.familybudget.ui.spendings.mappers.DetailsMapper
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val detailsModule = module {
    viewModelOf(::DetailViewModel)
    single { GetAllSpendingDetailsUseCase(get(), get<DetailsMapper>()) }
    singleOf(::DetailsMapper)
}
