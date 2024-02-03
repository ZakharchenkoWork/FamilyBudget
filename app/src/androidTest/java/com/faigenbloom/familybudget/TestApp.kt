package com.faigenbloom.familybudget

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.MockDataSource
import com.faigenbloom.familybudget.datasources.firebase.networkModule
import com.faigenbloom.familybudget.domain.domainModule
import com.faigenbloom.familybudget.repositories.repositoryModule
import com.faigenbloom.familybudget.ui.budget.budgetPageModule
import com.faigenbloom.familybudget.ui.categories.categoriesModule
import com.faigenbloom.familybudget.ui.family.familyPageModule
import com.faigenbloom.familybudget.ui.settings.settingsPageModule
import com.faigenbloom.familybudget.ui.spendings.detail.detailsModule
import com.faigenbloom.familybudget.ui.spendings.edit.spendingEditModule
import com.faigenbloom.familybudget.ui.spendings.list.spendingsPageModule
import com.faigenbloom.familybudget.ui.spendings.show.spendingShowModule
import com.faigenbloom.familybudget.ui.statistics.statisticsPageModule
import org.koin.dsl.module

class TestApp : App() {
    override fun onCreate() {
        super.onCreate()
        koinApplication.modules(
            mockedDataSource,
            networkModule,
            domainModule,
            repositoryModule,
            categoriesModule,
            spendingsPageModule,
            spendingEditModule,
            spendingShowModule,
            detailsModule,
            statisticsPageModule,
            budgetPageModule,
            settingsPageModule,
            familyPageModule,
        )
    }

    val mockedDataSource = module {
        single<BaseDataSource> { MockDataSource() }
    }
}

