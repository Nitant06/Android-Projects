package com.example.chitchat.feature_conversations.domain.repository

import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_conversations.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationsRepository{
    fun getConversations(): Flow<List<Conversation>>

    suspend fun deleteConversation(conversationId: String): Result<Unit>
}