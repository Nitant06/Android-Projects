package com.example.chitchat.feature_chat.domain.model

import com.google.firebase.Timestamp

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text:String = "",
    val timestamp: Timestamp = Timestamp.now()
)