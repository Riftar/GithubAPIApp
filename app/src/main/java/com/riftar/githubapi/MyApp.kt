package com.riftar.githubapi

import android.app.Application
import com.facebook.stetho.Stetho
import com.riftar.moviedb.di.dbModule
import com.riftar.moviedb.di.repositoryModule
import com.riftar.moviedb.di.uiModule
import org.koin.android.ext.android.startKoin

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(
            repositoryModule,
            uiModule
        ))
        Stetho.initializeWithDefaults(this)
    }
}