package com.example.chitchat.feature_profile.data.repository

import android.net.Uri
import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.core.domain.model.User
import com.example.chitchat.feature_profile.domain.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
): ProfileRepository{

    private val usersCollection = firestore.collection("users")
    private val currentUserId: String
        get() = auth.currentUser!!.uid

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val document = usersCollection.document(currentUserId).get().await()
            val user = document.toObject(User::class.java)!!
            Result.Success(user)
        }catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun uploadProfilePicture(imageUri: Uri): Result<String> {
        return try {
            val storageRef = storage.reference.child("profile_pictures/${currentUserId}")
            storageRef.putFile(imageUri).await()
            val downloadRef = storageRef.downloadUrl.await().toString()
            Result.Success(downloadRef)
        }catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return try {
            usersCollection.document(currentUserId).set(user).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun saveFcmToken(token: String): Result<Unit> {
        return try {
            usersCollection.document(currentUserId).update("fcmToken", token).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}