package com.faigenbloom.famillyspandings

import com.faigenbloom.famillyspandings.categories.CategoriesRepository
import com.faigenbloom.famillyspandings.datasources.MockDataSource
import com.faigenbloom.famillyspandings.spandings.SpendingsPageRepository
import com.faigenbloom.famillyspandings.spandings.SpendingsPageViewModel
import com.faigenbloom.famillyspandings.spandings.edit.SpendingEditViewModel
import com.faigenbloom.famillyspandings.spandings.edit.SpendingsEditRepository
import com.faigenbloom.famillyspandings.spandings.edit.detail.DetailViewModel
import com.faigenbloom.famillyspandings.spandings.edit.detail.DetailsRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

class TestApp : App() {
    override fun onCreate() {
        super.onCreate()
        koinApplication.modules(mockedModule)
    }

    val mockedModule = module {
        viewModelOf(::SpendingsPageViewModel)
        single { SpendingsPageRepository(get<MockDataSource>()) }
        viewModelOf(::SpendingEditViewModel)
        single { SpendingsEditRepository(get<MockDataSource>(), get()) }
        single { CategoriesRepository(get<MockDataSource>(), get()) }
        viewModelOf(::DetailViewModel)
        single { DetailsRepository(get<MockDataSource>()) }
    }
}

