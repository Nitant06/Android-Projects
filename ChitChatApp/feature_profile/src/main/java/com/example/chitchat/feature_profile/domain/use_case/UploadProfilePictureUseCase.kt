package com.example.chitchat.feature_profile.domain.use_case

import android.net.Uri
import com.example.chitchat.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UploadProfilePictureUseCase @Inject constructor(
    private val repository: ProfileRepository
){
    suspend operator fun invoke(imageUri: Uri) = repository.uploadProfilePicture(imageUri)
}