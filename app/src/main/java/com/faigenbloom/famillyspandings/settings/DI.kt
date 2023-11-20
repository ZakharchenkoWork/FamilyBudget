package com.faigenbloom.famillyspandings.settings

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.MockDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsPageModule = module {
    viewModelOf(::SettingsPageViewModel)
    singleOf(::SettingsPageRepository)
    singleOf(::MockDataSource) bind BaseDataSource::class
}
