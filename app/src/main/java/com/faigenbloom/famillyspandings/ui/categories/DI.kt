package com.faigenbloom.famillyspandings.ui.categories

import com.faigenbloom.famillyspandings.domain.categories.AddCategoryUseCase
import com.faigenbloom.famillyspandings.domain.categories.GetCategoriesUseCase
import com.faigenbloom.famillyspandings.domain.categories.UpdateCategoryPhotoUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val categoriesModule = module {
    viewModelOf(::CategoriesViewModel)
    singleOf(::CategoriesMapper)
    single { GetCategoriesUseCase(get(), get<CategoriesMapper>()) }
    singleOf(::AddCategoryUseCase)
    singleOf(::UpdateCategoryPhotoUseCase)
    singleOf(::AddCategoryUseCase)
}
