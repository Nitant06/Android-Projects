package com.example.chitchat.feature_auth.domain.use_case

import com.example.chitchat.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
){
    operator fun invoke() = repository.logout()
}