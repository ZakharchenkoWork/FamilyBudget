package com.faigenbloom.famillyspandings

import android.app.Application
import com.faigenbloom.famillyspandings.spandings_page.spendingsPageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {


        override fun onCreate() {
            super.onCreate()
            startKoin {
                androidContext(this@App)
                modules(
                    spendingsPageModule
                )
            }
        }

}