package com.example.chitchat.feature_auth.domain.use_case

import com.example.chitchat.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val repository: AuthRepository
){
    suspend operator fun invoke(email: String, password: String) = repository.signup(email, password)
}