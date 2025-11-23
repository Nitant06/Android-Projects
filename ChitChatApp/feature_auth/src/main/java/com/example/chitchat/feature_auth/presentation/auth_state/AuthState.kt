package com.example.chitchat.feature_auth.presentation.auth_state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun rememberAuthState(): State<FirebaseUser?> {
    val user = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    DisposableEffect(FirebaseAuth.getInstance()) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            user.value = auth.currentUser
        }
        FirebaseAuth.getInstance().addAuthStateListener(listener)
        onDispose {
            FirebaseAuth.getInstance().removeAuthStateListener(listener)
        }
    }
    return user
}