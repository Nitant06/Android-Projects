package com.example.chitchat.feature_profile.presentation.profile


import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_profile.domain.use_case.GetCurrentUserUseCase
import com.example.chitchat.feature_profile.domain.use_case.UpdateUserUseCase
import com.example.chitchat.feature_profile.domain.use_case.UploadProfilePictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            state = when (val result = getCurrentUserUseCase()) {
                is Result.Success -> state.copy(isLoading = false, user = result.data)
                is Result.Error -> state.copy(isLoading = false, error = result.exception.message)
            }
        }
    }

    fun onImageSelected(uri: Uri) {
        state = state.copy(selectedImageUri = uri)
    }

    fun onDisplayNameChange(newName: String) {
        state = state.copy(user = state.user?.copy(displayName = newName))
    }

    fun saveProfile() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            var photoUrl = state.user?.photoUrl

            if (state.selectedImageUri != null) {
                when (val uploadResult = uploadProfilePictureUseCase(state.selectedImageUri!!)) {
                    is Result.Success -> photoUrl = uploadResult.data
                    is Result.Error -> {
                        state = state.copy(isLoading = false, error = uploadResult.exception.message)
                        return@launch
                    }
                }
            }

            val updatedUser = state.user?.copy(photoUrl = photoUrl) ?: return@launch

            state = when (val updateResult = updateUserUseCase(updatedUser)) {
                is Result.Success -> state.copy(isLoading = false, error = null)
                is Result.Error -> state.copy(isLoading = false, error = updateResult.exception.message)
            }
        }
    }
}