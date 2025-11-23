package com.example.chitchat.feature_conversations.domain.use_case

import com.example.chitchat.feature_conversations.domain.repository.ConversationsRepository
import javax.inject.Inject

class GetConversationsUseCase @Inject constructor(
    private val repository: ConversationsRepository
){
    operator fun invoke() = repository.getConversations()
}