package com.faigenbloom.famillyspandings.spandings_page


import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val spendingsPageModule = module {
    viewModelOf(::SpendingsPageViewModel)
}