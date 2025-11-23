package com.example.chitchat.feature_chat.domain.repository

import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository{

    fun getMessages(conversationId: String): Flow<List<Message>>

    suspend fun sendMessage(conversationId: String, message: Message): Result<Unit>

    suspend fun getOrCreateConversation(otherUserId:String): Result<String>
}