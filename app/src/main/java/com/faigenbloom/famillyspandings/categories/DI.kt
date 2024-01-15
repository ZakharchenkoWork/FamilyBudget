package com.faigenbloom.famillyspandings.categories

import com.faigenbloom.famillyspandings.datasources.DatabaseDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val categoriesModule = module {
    viewModelOf(::CategoriesViewModel)
    single { CategoriesRepository(get<DatabaseDataSource>(), get()) }
}
