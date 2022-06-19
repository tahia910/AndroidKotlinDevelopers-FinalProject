package com.example.android.nextreminder.di

import android.app.Application
import com.example.android.nextreminder.data.network.ApiClient
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.network.SimilarService
import com.example.android.nextreminder.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

object KoinModule {

    fun startKoin(app: Application, koinApp: KoinApplication) {
        koinApp.run {
            androidContext(app)

            modules(
                listOf(
                    apiClientModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }

    private val apiClientModule: Module = module {
        single { ApiClient.retrofitService }
    }

    private val repositoryModule: Module = module {
        single { SimilarRepository((get() as SimilarService)) }
    }

    private val viewModelModule: Module = module {
        viewModel { HomeViewModel(get()) }
    }
}