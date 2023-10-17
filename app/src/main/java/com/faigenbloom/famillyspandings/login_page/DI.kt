package com.faigenbloom.famillyspandings.login_page


import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val loginPageModule = module {
    viewModelOf(::LoginPageViewModel)
}