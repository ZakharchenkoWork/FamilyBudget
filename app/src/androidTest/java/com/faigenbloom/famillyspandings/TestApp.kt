package com.faigenbloom.famillyspandings

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.MockDataSource
import com.faigenbloom.famillyspandings.domain.domainModule
import com.faigenbloom.famillyspandings.repositories.repositoryModule
import com.faigenbloom.famillyspandings.ui.budget.budgetPageModule
import com.faigenbloom.famillyspandings.ui.categories.categoriesModule
import com.faigenbloom.famillyspandings.ui.family.familyPageModule
import com.faigenbloom.famillyspandings.ui.settings.settingsPageModule
import com.faigenbloom.famillyspandings.ui.spandings.detail.detailsModule
import com.faigenbloom.famillyspandings.ui.spandings.edit.spendingEditModule
import com.faigenbloom.famillyspandings.ui.spandings.list.spendingsPageModule
import com.faigenbloom.famillyspandings.ui.spandings.show.spendingShowModule
import com.faigenbloom.famillyspandings.ui.statistics.statisticsPageModule
import org.koin.dsl.module

class TestApp : App() {
    override fun onCreate() {
        super.onCreate()
        koinApplication.modules(
            mockedDataSource,
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

