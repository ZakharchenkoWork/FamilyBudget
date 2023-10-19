package com.faigenbloom.famillyspandings.register

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val registerPageModule = module {
    viewModelOf(::RegisterPageViewModel)
}
