package com.example.chitchat.feature_conversations.presentation.new_chat

import com.example.chitchat.core.domain.model.User

data class NewChatState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)