package com.example.chitchat.feature_auth.data.repository

import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signup(email: String, password: String): Result<Unit> {
        return try {

            val authResult = firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            val firebaseUser = authResult.user!!

            val user = mapOf(
                "uid" to firebaseUser.uid,
                "email" to email,
                "displayName" to email.substringBefore('@'),
                "photoUrl" to null
            )

            firestore.collection("users").document(firebaseUser.uid).set(user).await()

            Result.Success(Unit)

        }catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email,password).await()
            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}