package com.example.chitchat.core.data.repository

import com.example.chitchat.core.domain.model.User
import com.example.chitchat.core.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.chitchat.core.domain.model.Result

class UserRepositoryImpl @Inject constructor(
    val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun getAllUsers(): Result<List<User>> {
        val currentUserId = auth.currentUser?.uid ?: return Result.Error(Exception("User not logged in"))
        return try {
            val result = usersCollection.whereNotEqualTo("uid", currentUserId).get().await()
            Result.Success(result.toObjects(User::class.java))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getUserById(userId: String): Result<User> {
        return try {
            val document = usersCollection.document(userId).get().await()
            val user = document.toObject(User::class.java)!!
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}