package com.example.android.nextreminder.di

import android.app.Application
import com.example.android.nextreminder.data.ImageRepository
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.local.LocalDatabase
import com.example.android.nextreminder.data.network.ApiClient
import com.example.android.nextreminder.ui.detail.DetailViewModel
import com.example.android.nextreminder.ui.main.MainViewModel
import com.example.android.nextreminder.ui.searchresult.SearchResultViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.dsl.module

object KoinModule {

    fun startKoin(app: Application, koinApp: KoinApplication) {
        koinApp.run {
            androidContext(app)

            val module = module {
                single { ApiClient.similarService }
                single { ApiClient.imageService }
                single { LocalDatabase.createBookmarkDao(app) }
                single { SimilarRepository(get(), get()) }
                single { ImageRepository(get()) }
                viewModel { MainViewModel(get()) }
                viewModel { SearchResultViewModel(get()) }
                viewModel { DetailViewModel(get(), get()) }
            }
            modules(listOf(module))
        }
    }
}