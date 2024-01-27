package com.faigenbloom.famillyspandings.ui.spendings.list

import com.faigenbloom.famillyspandings.domain.categories.GetCategoryByIdUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetAllSpendingsUseCase
import com.faigenbloom.famillyspandings.domain.spendings.SortPlatesUseCase
import com.faigenbloom.famillyspandings.domain.spendings.SpendingsPagingSource
import com.faigenbloom.famillyspandings.ui.categories.CategoriesMapper
import com.faigenbloom.famillyspandings.ui.spendings.mappers.SpendingsMapper
import com.faigenbloom.famillyspandings.ui.spendings.mappers.SpendingsWithCategoryMapper
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val spendingsPageModule = module {
    viewModelOf(::SpendingsListViewModel)
    singleOf(::SpendingsMapper)
    singleOf(::CategoriesMapper)
    singleOf(::SpendingsWithCategoryMapper)
    single { SortPlatesUseCase<SpendingCategoryUiData>() }
    single { GetCategoryByIdUseCase(get(), get<CategoriesMapper>()) }
    single { GetAllSpendingsUseCase(get(), get<SpendingsMapper>()) }
    singleOf(::SpendingsPagingSource)
}
