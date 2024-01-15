package com.faigenbloom.famillyspandings.spandings.edit.detail

import com.faigenbloom.famillyspandings.datasources.DatabaseDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val detailsModule = module {
    viewModelOf(::DetailViewModel)
    single { DetailsRepository(get<DatabaseDataSource>()) }
}
