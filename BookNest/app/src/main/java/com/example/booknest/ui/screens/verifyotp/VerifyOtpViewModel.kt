package com.example.booknest.ui.screens.verifyotp

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknest.data.repository.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class VerifyOtpUiState(
    val otp: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignInSuccessful: Boolean = false,
    val countDownSeconds: Int = 60,
    val canResend: Boolean = false,
    val newVerificationId: String? = null
)

@HiltViewModel
class VerifyOtpViewModel @Inject constructor (
    private val repository: AuthRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _uiState = MutableStateFlow(VerifyOtpUiState())
    val uiState = _uiState.asStateFlow()

    private var countdownJob: Job? = null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val verificationId: String = requireNotNull(savedStateHandle["verificationId"])
    private val phoneNumber: String = "+91${requireNotNull(savedStateHandle["phoneNumber"])}"

    private val userName: String = savedStateHandle["name"] ?: ""
    private val userEmail: String = savedStateHandle["email"] ?: ""

    init {
        startCountdown()
    }

    fun onOtpChange(otp: String) {
        if(otp.length <= 6){
            _uiState.update { it.copy(otp = otp) }
        }
    }

    private fun startCountdown() {
        _uiState.update { it.copy(canResend = false, countDownSeconds = 60) }
        countdownJob?.cancel() // Cancel any existing job
        countdownJob = viewModelScope.launch {
            for (i in 60 downTo 1) {
                _uiState.update { it.copy(countDownSeconds = i) }
                delay(1000)
            }
            _uiState.update { it.copy(canResend = true) }
        }
    }

    fun verifyOtp() {
        if (uiState.value.otp.length != 6) {
            _uiState.update { it.copy(error = "Please enter a 6-digit OTP.") }
            return
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        val credential = PhoneAuthProvider.getCredential(
            uiState.value.newVerificationId ?: verificationId, // Use new ID if resent
            uiState.value.otp
        )
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            try {
                // 1. Sign in
                auth.signInWithCredential(credential).await()

                // 2. THIS IS THE FIX: Update Firebase Profile
                // This ensures checkout screen can read the name/email later
                repository.updateUserProfile(userName, userEmail)

                // 3. Update UI to Success
                _uiState.update { it.copy(isLoading = false, isSignInSuccessful = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Verification failed: ${e.message}") }
            }
        }
    }

    fun resendOtp(activity: Activity) {
        _uiState.update { it.copy(isLoading = true, error = null, otp = "") }
        startCountdown()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    _uiState.update { it.copy(isLoading = false) }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _uiState.update { it.copy(isLoading = false, error = "Resend failed: ${e.message}") }
                }

                override fun onCodeSent(newVerificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    _uiState.update { it.copy(isLoading = false, newVerificationId = newVerificationId) }
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }

}





