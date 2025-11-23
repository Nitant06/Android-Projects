package com.example.chitchat.feature_conversations

import com.example.chitchat.feature_conversations.data.repository.ConversationsRepositoryImpl
import com.example.chitchat.feature_conversations.domain.repository.ConversationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConversationsModule {

    @Binds
    @Singleton
    abstract fun bindConversationsRepository(impl: ConversationsRepositoryImpl): ConversationsRepository

}