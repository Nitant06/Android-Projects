package com.example.chitchat.core.domain.use_case

import com.example.chitchat.core.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String) = repository.getUserById(userId)
}