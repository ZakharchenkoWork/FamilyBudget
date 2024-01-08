package com.faigenbloom.famillyspandings.statistics

import com.faigenbloom.famillyspandings.datasources.DatabaseDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val statisticsPageModule = module {
    viewModelOf(::StatisticsPageViewModel)
    single { StatisticsPageRepository(get<DatabaseDataSource>()) }
}
