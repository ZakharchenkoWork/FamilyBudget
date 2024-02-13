package com.faigenbloom.familybudget.ui.settings

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val settingsPageModule = module {
    viewModelOf(::SettingsPageViewModel)
}
