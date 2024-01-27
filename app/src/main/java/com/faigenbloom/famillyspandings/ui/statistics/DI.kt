package com.faigenbloom.famillyspandings.ui.statistics

import com.faigenbloom.famillyspandings.domain.categories.GetCategoriesUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetAllSpendingsUseCase
import com.faigenbloom.famillyspandings.domain.statistics.GetCategorySummariesUseCase
import com.faigenbloom.famillyspandings.ui.categories.CategoriesMapper
import com.faigenbloom.famillyspandings.ui.spendings.mappers.SpendingsMapper
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val statisticsPageModule = module {
    viewModelOf(::StatisticsPageViewModel)
    singleOf(::GetCategorySummariesUseCase)

    singleOf(::CategoriesMapper)
    single { GetCategoriesUseCase(get(), get<CategoriesMapper>()) }

    singleOf(::SpendingsMapper)
    single { GetAllSpendingsUseCase(get(), get<SpendingsMapper>()) }
}
