package com.example.booknest.data.repository

import com.example.booknest.ui.screens.checkout.UserDetails

interface  AuthRepository{
    fun logout()

    suspend fun updateUserProfile(name: String, email: String)

    suspend fun getUserDetails(userId: String): UserDetails
}