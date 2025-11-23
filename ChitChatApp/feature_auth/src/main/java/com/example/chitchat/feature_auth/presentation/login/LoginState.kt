package com.example.chitchat.feature_auth.presentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val isLoginSuccess: Boolean = false
)