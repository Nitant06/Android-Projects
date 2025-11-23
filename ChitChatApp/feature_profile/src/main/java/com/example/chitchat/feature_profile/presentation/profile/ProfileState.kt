package com.example.chitchat.feature_profile.presentation.profile

import android.net.Uri
import com.example.chitchat.core.domain.model.User

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedImageUri: Uri? = null
)