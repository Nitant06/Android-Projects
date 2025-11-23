package com.example.chitchat.feature_profile.domain.use_case

import com.example.chitchat.core.domain.model.User
import com.example.chitchat.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(user: User) = repository.updateUser(user)
}
