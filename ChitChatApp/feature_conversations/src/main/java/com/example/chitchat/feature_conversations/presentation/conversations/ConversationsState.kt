package com.example.chitchat.feature_conversations.presentation.conversations

import com.example.chitchat.feature_conversations.domain.model.Conversation

data class ConversationsState(
    val isLoading: Boolean = false,
    val conversations: List<Conversation> = emptyList(),
    val error: String? = null
)
