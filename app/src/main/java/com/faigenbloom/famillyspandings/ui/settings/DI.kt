package com.faigenbloom.famillyspandings.ui.settings

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val settingsPageModule = module {
    viewModelOf(::SettingsPageViewModel)
    singleOf(::SettingsPageRepository)
}
