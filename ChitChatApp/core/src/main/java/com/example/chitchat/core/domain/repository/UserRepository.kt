package com.example.chitchat.core.domain.repository

import com.example.chitchat.core.domain.model.User
import com.example.chitchat.core.domain.model.Result

interface UserRepository {
    suspend fun getAllUsers(): Result<List<User>>
    suspend fun getUserById(userId: String): Result<User>
}