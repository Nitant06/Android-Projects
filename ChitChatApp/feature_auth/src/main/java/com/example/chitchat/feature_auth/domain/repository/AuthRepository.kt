package com.example.chitchat.feature_auth.domain.repository

import com.example.chitchat.core.domain.model.Result

interface AuthRepository{

    suspend fun signup(email: String, password: String) : Result<Unit>

    suspend fun login(email: String,password: String): Result<Unit>

    fun logout()

}