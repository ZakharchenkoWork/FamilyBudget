package com.faigenbloom.familybudget.ui.family

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val familyPageModule = module {
    viewModelOf(::FamilyPageViewModel)
}
