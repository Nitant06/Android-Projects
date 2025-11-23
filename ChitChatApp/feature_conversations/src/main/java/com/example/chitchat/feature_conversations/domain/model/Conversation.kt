package com.example.chitchat.feature_conversations.domain.model

import com.example.chitchat.core.domain.model.User
import com.google.firebase.Timestamp

data class Conversation(
    val id: String = "",
    val lastMessageText: String? = null,
    val lastMessageTimestamp: Timestamp? = null,
    val lastMessageSenderId: String? = null,
    val otherUser: User? = null
)