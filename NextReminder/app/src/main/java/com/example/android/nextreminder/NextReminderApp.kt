package com.example.android.nextreminder

import android.app.Application
import com.example.android.nextreminder.di.KoinModule
import org.koin.core.context.startKoin

class NextReminderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            KoinModule.startKoin(this@NextReminderApp, this)
        }
    }
}