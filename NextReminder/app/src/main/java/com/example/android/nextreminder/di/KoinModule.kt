package com.example.android.nextreminder.di

import android.app.Application
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.local.LocalDatabase
import com.example.android.nextreminder.data.network.ApiClient
import com.example.android.nextreminder.data.network.SimilarService
import com.example.android.nextreminder.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.dsl.module

object KoinModule {

    fun startKoin(app: Application, koinApp: KoinApplication) {
        koinApp.run {
            androidContext(app)

            val module = module {
                single { ApiClient.retrofitService }
                single { LocalDatabase.createBookmarkDao(app) }
                single { SimilarRepository((get() as SimilarService), get()) }
                viewModel { HomeViewModel(get()) }
            }
            modules(listOf(module))
        }
    }
}