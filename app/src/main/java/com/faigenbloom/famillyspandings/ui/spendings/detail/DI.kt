package com.faigenbloom.famillyspandings.ui.spendings.detail

import com.faigenbloom.famillyspandings.domain.details.GetAllSpendingDetailsUseCase
import com.faigenbloom.famillyspandings.ui.spendings.mappers.DetailsMapper
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val detailsModule = module {
    viewModelOf(::DetailViewModel)
    single { GetAllSpendingDetailsUseCase(get(), get<DetailsMapper>()) }
    singleOf(::DetailsMapper)
}
