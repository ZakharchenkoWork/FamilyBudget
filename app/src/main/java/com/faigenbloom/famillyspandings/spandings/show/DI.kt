package com.faigenbloom.famillyspandings.spandings.show

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.MockDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val spendingShowModule = module {
    viewModelOf(::SpendingShowViewModel)
    singleOf(::SpendingsRepository)
    singleOf(::MockDataSource) bind BaseDataSource::class
}
