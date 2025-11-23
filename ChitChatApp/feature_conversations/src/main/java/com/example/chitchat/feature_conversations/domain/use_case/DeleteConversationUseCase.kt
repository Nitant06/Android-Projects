package com.example.chitchat.feature_conversations.domain.use_case

import com.example.chitchat.feature_conversations.domain.repository.ConversationsRepository
import javax.inject.Inject


class DeleteConversationUseCase @Inject constructor(
    private val repository: ConversationsRepository
) {
    suspend operator fun invoke(conversationId: String) = repository.deleteConversation(conversationId)
}