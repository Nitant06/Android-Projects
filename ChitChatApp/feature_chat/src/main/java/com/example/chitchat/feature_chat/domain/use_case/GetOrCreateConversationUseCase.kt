package com.example.chitchat.feature_chat.domain.use_case

import com.example.chitchat.feature_chat.domain.repository.ChatRepository
import javax.inject.Inject

class GetOrCreateConversationUseCase @Inject constructor(
    private val repository: ChatRepository
){
    suspend operator fun invoke(otherUserId:String) = repository.getOrCreateConversation(otherUserId)
}