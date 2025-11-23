package com.example.chitchat.feature_chat.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.core.domain.use_case.GetUserByIdUseCase
import com.example.chitchat.feature_chat.domain.model.Message
import com.example.chitchat.feature_chat.domain.service.SmartReplyService
import com.example.chitchat.feature_chat.domain.use_case.GetMessagesUseCase
import com.example.chitchat.feature_chat.domain.use_case.GetOrCreateConversationUseCase
import com.example.chitchat.feature_chat.domain.use_case.SendMessageUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getOrCreateConversationUseCase: GetOrCreateConversationUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    savedStateHandle: SavedStateHandle,
    private val firebaseAuth: FirebaseAuth,
    private val smartReplyService: SmartReplyService
): ViewModel(){
    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions = _suggestions.asStateFlow()

    private var conversationId: String? = null

    init {
        val otherUserId = savedStateHandle.get<String>("otherUserId")
        if (otherUserId == null) {
            _state.update { it.copy(isLoading = false, error = "User not found.") }
        } else {
            loadOtherUserDetails(otherUserId)
            getConversationId(otherUserId)
        }
    }

    private fun loadOtherUserDetails(userId: String) {
        viewModelScope.launch {
            when (val result = getUserByIdUseCase(userId)) {
                is Result.Success -> _state.update { it.copy(otherUser = result.data) }
                is Result.Error -> _state.update { it.copy(error = result.exception.message) }
            }
        }
    }

    private fun getConversationId(otherUserId:String){
        viewModelScope.launch {
            when(val result = getOrCreateConversationUseCase(otherUserId)){
                is Result.Success -> {
                    conversationId = result.data
                    listenForMessages(result.data)
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.exception.message) }
                }
            }
        }
    }

    private fun listenForMessages(convId:String){
        viewModelScope.launch {
            getMessagesUseCase(convId)
                .onEach { messages ->
                    val currentUserId = firebaseAuth.currentUser?.uid
                    if (messages.isNotEmpty() && messages.last().senderId != currentUserId) {
                        generateSmartReplies(messages)
                    } else {
                        _suggestions.value = emptyList()
                    }
                    _state.update { it.copy(isLoading = false, messages = messages) }
                    generateSmartReplies(messages)
                }
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect()
            }
        }

    private fun generateSmartReplies(messages: List<Message>) {
        if (messages.isEmpty()) return
        viewModelScope.launch {
            when (val result = smartReplyService.generateReplies(messages)) {
                is Result.Success -> {
                    _suggestions.value = result.data
                }
                is Result.Error -> {
                    _suggestions.value = emptyList()
                }
            }
        }
    }


    fun sendMessage(text: String){
        val convId = conversationId?:return
        val currentUserId = firebaseAuth.currentUser?.uid?:return

        if (text.isNotBlank()) {
            val message = Message(
                senderId = currentUserId,
                text = text.trim()
            )
            viewModelScope.launch {
                sendMessageUseCase(convId, message)
            }
        }
    }

}