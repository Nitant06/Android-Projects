package com.example.chitchat.feature_chat

import com.example.chitchat.feature_chat.data.repository.ChatRepositoryImpl
import com.example.chitchat.feature_chat.data.service.SmartReplyServiceImpl
import com.example.chitchat.feature_chat.domain.repository.ChatRepository
import com.example.chitchat.feature_chat.domain.service.SmartReplyService
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplyGenerator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindSmartReplyService(impl: SmartReplyServiceImpl): SmartReplyService

    companion object {
        @Provides
        @Singleton
        fun provideSmartReplyClient(): SmartReplyGenerator = SmartReply.getClient()
    }
}