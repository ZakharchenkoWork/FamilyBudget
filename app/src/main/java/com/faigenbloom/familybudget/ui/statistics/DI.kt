package com.faigenbloom.familybudget.ui.statistics

import com.faigenbloom.familybudget.domain.categories.GetCategoriesUseCase
import com.faigenbloom.familybudget.domain.spendings.GetAllSpendingsUseCase
import com.faigenbloom.familybudget.domain.statistics.GetCategorySummariesUseCase
import com.faigenbloom.familybudget.ui.categories.CategoriesMapper
import com.faigenbloom.familybudget.ui.spendings.mappers.SpendingsMapper
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
