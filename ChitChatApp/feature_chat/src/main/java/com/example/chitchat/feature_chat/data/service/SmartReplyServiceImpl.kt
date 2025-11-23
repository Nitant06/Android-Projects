package com.example.chitchat.feature_chat.data.service

import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_chat.domain.model.Message
import com.example.chitchat.feature_chat.domain.service.SmartReplyService
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.nl.smartreply.SmartReplyGenerator
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class SmartReplyServiceImpl @Inject constructor(
    private val smartReply: SmartReplyGenerator,
    private val auth: FirebaseAuth
): SmartReplyService{

    override suspend fun generateReplies(conversationHistory: List<Message>): Result<List<String>> {
        val currentUserId = auth.currentUser?.uid ?: return Result.Error(Exception("User not logged in"))

        val chatHistory = conversationHistory.map { message ->
            if (message.senderId == currentUserId) {
                TextMessage.createForLocalUser(message.text, message.timestamp.seconds * 1000)
            } else {
                TextMessage.createForRemoteUser(
                    message.text,
                    message.timestamp.seconds * 1000,
                    message.senderId
                )
            }
        }

        if (chatHistory.lastOrNull()?.isLocalUser == true) {
            return Result.Success(emptyList())
        }

        return try {
            val result = smartReply.suggestReplies(chatHistory).await()

            if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
                Result.Success(result.suggestions.map { it.text })
            } else {
                Result.Success(emptyList())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}