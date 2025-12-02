package com.nitant.docmind.di

import com.nitant.docmind.data.GeminiService
import com.nitant.docmind.data.NetworkClient
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single { NetworkClient.client }
}

// 2. Init Koin
fun initKoin(
    apiKey: String,
    platformModule: org.koin.core.module.Module,
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    appDeclaration()
    modules(
        appModule,
        platformModule,
        module {
            single { GeminiService(client = get(), apiKey = apiKey) }
        }
    )
}