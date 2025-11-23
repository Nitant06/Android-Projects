package com.example.chitchat.feature_chat.presentation.chat

import com.example.chitchat.core.domain.model.User
import com.example.chitchat.feature_chat.domain.model.Message

data class ChatState(
    val isLoading: Boolean = true,
    val messages: List<Message> = emptyList(),
    val otherUser: User? = null,
    val error: String? = null
)
