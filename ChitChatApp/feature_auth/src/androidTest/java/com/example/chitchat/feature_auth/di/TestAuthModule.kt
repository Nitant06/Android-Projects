package com.example.chitchat.feature_auth.di

import com.example.chitchat.feature_auth.AuthModule
import com.example.chitchat.feature_auth.data.FakeAuthRepository
import com.example.chitchat.feature_auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthModule::class]
)
object TestAuthModule {

    @Provides
    @Singleton
    fun provideFakeAuthRepository(): AuthRepository {
        return FakeAuthRepository()
    }
}