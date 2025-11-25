package com.example.booknest.di

import com.example.booknest.data.repository.AuthRepository
import com.example.booknest.data.repository.AuthRepositoryImpl
import com.example.booknest.data.repository.HomeRepository
import com.example.booknest.data.repository.HomeRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideHomeRepository(db: FirebaseDatabase): HomeRepository {
        return HomeRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, db: FirebaseDatabase): AuthRepository {
        return AuthRepositoryImpl(auth,db)
    }
}