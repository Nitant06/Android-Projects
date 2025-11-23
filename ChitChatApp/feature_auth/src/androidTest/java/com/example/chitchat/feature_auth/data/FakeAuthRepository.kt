package com.example.chitchat.feature_auth.data

import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_auth.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {

    var signupShouldReturnError = false
    private val errorMessage = "Invalid email or password"

    override suspend fun signup(email: String, password: String): Result<Unit> {
        return if (signupShouldReturnError || email.isBlank() || password.isBlank()) {
            Result.Error(Exception(errorMessage))
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return Result.Success(Unit)
    }

    override fun logout() {}
}