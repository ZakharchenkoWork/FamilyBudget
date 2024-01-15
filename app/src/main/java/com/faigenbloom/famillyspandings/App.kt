package com.faigenbloom.famillyspandings

import android.app.Application
import com.faigenbloom.famillyspandings.budget.budgetPageModule
import com.faigenbloom.famillyspandings.categories.categoriesModule
import com.faigenbloom.famillyspandings.datasources.databaseModule
import com.faigenbloom.famillyspandings.family.familyPageModule
import com.faigenbloom.famillyspandings.id.idGeneratorModule
import com.faigenbloom.famillyspandings.login.loginPageModule
import com.faigenbloom.famillyspandings.register.registerPageModule
import com.faigenbloom.famillyspandings.settings.settingsPageModule
import com.faigenbloom.famillyspandings.spandings.edit.detail.detailsModule
import com.faigenbloom.famillyspandings.spandings.edit.spendingEditModule
import com.faigenbloom.famillyspandings.spandings.show.spendingShowModule
import com.faigenbloom.famillyspandings.spandings.spendingsPageModule
import com.faigenbloom.famillyspandings.statistics.statisticsPageModule
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
                idGeneratorModule,
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
