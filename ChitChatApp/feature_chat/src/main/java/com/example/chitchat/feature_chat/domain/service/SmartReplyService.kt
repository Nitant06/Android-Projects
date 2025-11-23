package com.example.chitchat.feature_chat.domain.service

import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_chat.domain.model.Message

interface SmartReplyService{

    suspend fun generateReplies(conversationHistory:List<Message>): Result<List<String>>
}