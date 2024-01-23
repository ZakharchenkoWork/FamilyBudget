package com.faigenbloom.famillyspandings

import android.app.Application
import com.faigenbloom.famillyspandings.datasources.databaseModule
import com.faigenbloom.famillyspandings.domain.domainModule
import com.faigenbloom.famillyspandings.repositories.repositoryModule
import com.faigenbloom.famillyspandings.ui.budget.budgetPageModule
import com.faigenbloom.famillyspandings.ui.categories.categoriesModule
import com.faigenbloom.famillyspandings.ui.family.familyPageModule
import com.faigenbloom.famillyspandings.ui.login.loginPageModule
import com.faigenbloom.famillyspandings.ui.register.registerPageModule
import com.faigenbloom.famillyspandings.ui.settings.settingsPageModule
import com.faigenbloom.famillyspandings.ui.spendings.detail.detailsModule
import com.faigenbloom.famillyspandings.ui.spendings.edit.spendingEditModule
import com.faigenbloom.famillyspandings.ui.spendings.list.spendingsPageModule
import com.faigenbloom.famillyspandings.ui.spendings.show.spendingShowModule
import com.faigenbloom.famillyspandings.ui.statistics.statisticsPageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

open class App : Application() {
    protected lateinit var koinApplication: KoinApplication
    override fun onCreate() {
        super.onCreate()
        koinApplication = startKoin {
            androidContext(this@App)
            modules(
                databaseModule,
                domainModule,
                repositoryModule,
                loginPageModule,
                registerPageModule,
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
    }
}
