package com.nitant.docmind

import android.app.Application
import com.nitant.docmind.data.repository.DocRepositoryImpl
import com.nitant.docmind.db.AppDatabase
import com.nitant.docmind.di.initKoin
import com.nitant.docmind.domain.repository.DocRepository
import com.nitant.docmind.presentation.home.HomeViewModel
import com.nitant.docmind.presentation.scan.ScanViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class DocMindApplication : Application(){
    override fun onCreate(){
        super.onCreate()

        val androidModule = module {
            // 1. Database
            single { AppDatabase.getDatabase(androidContext()).documentDao() }

            // 2. Repository Implementation
            single<DocRepository> {
                DocRepositoryImpl(dao = get(), aiService = get())
            }

            // 3. ViewModels
            viewModel { HomeViewModel(repository = get()) }
            viewModel { ScanViewModel(repository = get()) }
        }

        // Initialize Koin with the Android Module
        initKoin(
            apiKey = BuildConfig.GEMINI_API_KEY,
            platformModule = androidModule
        ) {
            androidContext(this@DocMindApplication)
        }

    }
}