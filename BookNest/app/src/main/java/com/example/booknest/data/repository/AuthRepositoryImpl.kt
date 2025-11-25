package com.example.booknest.data.repository

import com.example.booknest.ui.screens.checkout.UserDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
): AuthRepository{

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun updateUserProfile(name: String, email: String) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val uid = user.uid

            val userMap = mapOf(
                "name" to name,
                "email" to email,
                "phone" to (user.phoneNumber ?: "")
            )

            try {
                firebaseDatabase.getReference("users")
                    .child(uid)
                    .setValue(userMap)
                    .await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getUserDetails(userId: String): UserDetails {
        return try {
            val snapshot = firebaseDatabase.getReference("users")
                .child(userId)
                .get()
                .await()

            UserDetails(
                name = snapshot.child("name").value as? String,
                email = snapshot.child("email").value as? String,
                phone = snapshot.child("phone").value as? String
            )
        } catch (e: Exception) {
            e.printStackTrace()
            UserDetails()
        }
    }


}