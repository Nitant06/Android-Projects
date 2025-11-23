package com.example.chitchat.feature_conversations.presentation.new_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.core.domain.use_case.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewChatViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewChatState())
    val state = _state.asStateFlow()

    init {
        loadAllUsers()
    }

    private fun loadAllUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getAllUsersUseCase()) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false, users = result.data) }
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.exception.message) }
                }
            }
        }
    }
}