package com.faigenbloom.famillyspandings.spandings.show

import com.faigenbloom.famillyspandings.datasources.DatabaseDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val spendingShowModule = module {
    viewModelOf(::SpendingShowViewModel)
    single { SpendingsRepository(get<DatabaseDataSource>()) }
}
