package com.example.chitchat.feature_profile.domain.repository

import android.net.Uri
import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.core.domain.model.User


interface ProfileRepository{

    suspend fun getCurrentUser(): Result<User>

    suspend fun uploadProfilePicture(imageUri: Uri): Result<String>

    suspend fun updateUser(user: User): Result<Unit>

    suspend fun saveFcmToken(token: String): Result<Unit>

}