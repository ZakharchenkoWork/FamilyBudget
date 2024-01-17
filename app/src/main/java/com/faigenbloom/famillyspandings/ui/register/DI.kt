package com.faigenbloom.famillyspandings.ui.register

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val registerPageModule = module {
    viewModelOf(::RegisterPageViewModel)
    singleOf(::RegisterRepository)
}
