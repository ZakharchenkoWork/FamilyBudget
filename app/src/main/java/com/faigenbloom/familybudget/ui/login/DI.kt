package com.faigenbloom.familybudget.ui.login

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val loginPageModule = module {
    viewModelOf(::LoginPageViewModel)
}
