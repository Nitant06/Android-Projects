package com.example.chitchat.feature_auth.presentation.signup

data class SignupState(
    val isLoading: Boolean = false,
    val signupError: String? = null,
    val isSignupSuccess: Boolean = false
)