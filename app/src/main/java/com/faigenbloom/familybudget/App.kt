package com.faigenbloom.familybudget

import android.app.Application
import com.faigenbloom.familybudget.datasources.databaseModule
import com.faigenbloom.familybudget.datasources.firebase.networkModule
import com.faigenbloom.familybudget.domain.domainModule
import com.faigenbloom.familybudget.domain.mappers.domainMappers
import com.faigenbloom.familybudget.repositories.repositoryModule
import com.faigenbloom.familybudget.ui.budget.budgetPageModule
import com.faigenbloom.familybudget.ui.categories.categoriesModule
import com.faigenbloom.familybudget.ui.family.familyPageModule
import com.faigenbloom.familybudget.ui.login.loginPageModule
import com.faigenbloom.familybudget.ui.onboarding.onboardingModule
import com.faigenbloom.familybudget.ui.register.registerPageModule
import com.faigenbloom.familybudget.ui.settings.settingsPageModule
import com.faigenbloom.familybudget.ui.spendings.detail.detailsModule
import com.faigenbloom.familybudget.ui.spendings.edit.spendingEditModule
import com.faigenbloom.familybudget.ui.spendings.list.spendingsPageModule
import com.faigenbloom.familybudget.ui.spendings.show.spendingShowModule
import com.faigenbloom.familybudget.ui.statistics.statisticsPageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File

open class App : Application() {
    protected lateinit var koinApplication: KoinApplication
    override fun onCreate() {
        super.onCreate()

        koinApplication = startKoin {
            androidContext(this@App)
            modules(
                databaseModule,
                networkModule,
                imageStoreDirectoryModule,
                domainModule,
                domainMappers,
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
                onboardingModule,
            )
        }
    }

    private val imageStoreDirectoryModule = module {
        single<File> {
            val mediaDir = externalMediaDirs.firstOrNull()?.let {
                File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
            }

            if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
        }
    }
}
