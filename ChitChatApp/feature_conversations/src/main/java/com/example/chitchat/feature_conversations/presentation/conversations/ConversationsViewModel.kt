package com.example.chitchat.feature_conversations.presentation.conversations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_auth.domain.use_case.LogoutUseCase
import com.example.chitchat.feature_conversations.domain.use_case.DeleteConversationUseCase
import com.example.chitchat.core.domain.use_case.GetAllUsersUseCase
import com.example.chitchat.feature_conversations.domain.use_case.GetConversationsUseCase
import com.example.chitchat.feature_profile.domain.repository.ProfileRepository
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val deleteConversationUseCase: DeleteConversationUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ConversationsState())
    val state = _state.asStateFlow()

    init {
        listenForData()
        updateFcmToken()
    }

    private fun listenForData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val allUsersResult = getAllUsersUseCase()
            if (allUsersResult is Result.Error) {
                _state.update { it.copy(isLoading = false, error = allUsersResult.exception.message) }
                return@launch
            }
            val allUsers = (allUsersResult as Result.Success).data
            val usersMap = allUsers.associateBy { it.uid }

            getConversationsUseCase()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { conversations ->
                    val enrichedConversations = conversations.mapNotNull { conversation ->
                        val otherUserId = conversation.otherUser?.uid
                        val otherUserDetails = usersMap[otherUserId]
                        otherUserDetails?.let {
                            conversation.copy(otherUser = it)
                        }
                    }
                    _state.update { it.copy(isLoading = false, conversations = enrichedConversations) }
                }
        }
    }

    private fun updateFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                viewModelScope.launch {
                    profileRepository.saveFcmToken(token)
                }
            }
        }
    }

    fun onDeleteConversation(conversationId: String) {
        viewModelScope.launch {
            deleteConversationUseCase(conversationId)
        }
    }

    fun onLogoutClick() {
        logoutUseCase()
    }
}