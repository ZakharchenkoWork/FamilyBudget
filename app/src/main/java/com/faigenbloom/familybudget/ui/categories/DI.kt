package com.faigenbloom.familybudget.ui.categories

import com.faigenbloom.familybudget.domain.categories.GetCategoriesUseCase
import com.faigenbloom.familybudget.domain.categories.SetCategoryUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val categoriesModule = module {
    viewModelOf(::CategoriesViewModel)
    singleOf(::CategoriesMapper)
    single { GetCategoriesUseCase(get(), get<CategoriesMapper>()) }
    singleOf(::SetCategoryUseCase)
    singleOf(::SetCategoryUseCase)
}
