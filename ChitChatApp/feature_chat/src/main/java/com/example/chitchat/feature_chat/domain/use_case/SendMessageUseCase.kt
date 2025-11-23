package com.example.chitchat.feature_chat.domain.use_case

import com.example.chitchat.feature_chat.domain.model.Message
import com.example.chitchat.feature_chat.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
){
    suspend operator fun invoke(conversationId:String,message: Message) = repository.sendMessage(conversationId,message)
}