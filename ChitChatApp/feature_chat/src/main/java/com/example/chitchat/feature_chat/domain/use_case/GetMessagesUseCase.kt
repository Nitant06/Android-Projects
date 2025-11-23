package com.example.chitchat.feature_chat.domain.use_case

import com.example.chitchat.feature_chat.domain.repository.ChatRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
){
     operator fun invoke(conversationId: String) = repository.getMessages(conversationId)
}