package com.faigenbloom.famillyspandings

import android.app.Application
import com.faigenbloom.famillyspandings.edit.spendingEditModule
import com.faigenbloom.famillyspandings.login.loginPageModule
import com.faigenbloom.famillyspandings.register.registerPageModule
import com.faigenbloom.famillyspandings.spandings.spendingsPageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                spendingsPageModule,
                loginPageModule,
                registerPageModule,
                spendingEditModule,
            )
        }
    }
}
