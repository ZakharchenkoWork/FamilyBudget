package com.faigenbloom.famillyspandings.statistics

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.MockDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val statisticsPageModule = module {
    viewModelOf(::StatisticsPageViewModel)
    singleOf(::StatisticsPageRepository)
    singleOf(::MockDataSource) bind BaseDataSource::class
}
