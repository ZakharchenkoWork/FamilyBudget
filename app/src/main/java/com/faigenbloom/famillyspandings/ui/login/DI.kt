package com.faigenbloom.famillyspandings.ui.login

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val loginPageModule = module {
    viewModelOf(::LoginPageViewModel)
    singleOf(::LoginRepository)
}
