package com.example.chitchat.feature_profile.domain.use_case

import com.example.chitchat.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: ProfileRepository
){
    suspend operator fun invoke() = repository.getCurrentUser()
}
