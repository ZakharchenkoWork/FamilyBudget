package com.faigenbloom.famillyspandings

import android.app.Application
import com.faigenbloom.famillyspandings.budget.budgetPageModule
import com.faigenbloom.famillyspandings.categories.categoriesModule
import com.faigenbloom.famillyspandings.datasources.databaseModule
import com.faigenbloom.famillyspandings.family.familyPageModule
import com.faigenbloom.famillyspandings.login.loginPageModule
import com.faigenbloom.famillyspandings.register.registerPageModule
import com.faigenbloom.famillyspandings.settings.settingsPageModule
import com.faigenbloom.famillyspandings.spandings.edit.spendingEditModule
import com.faigenbloom.famillyspandings.spandings.show.spendingShowModule
import com.faigenbloom.famillyspandings.spandings.spendingsPageModule
import com.faigenbloom.famillyspandings.statistics.statisticsPageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                databaseModule,
                loginPageModule,
                registerPageModule,
                categoriesModule,
                spendingsPageModule,
                spendingEditModule,
                spendingShowModule,
                statisticsPageModule,
                budgetPageModule,
                settingsPageModule,
                familyPageModule,
            )
        }
    }
}
