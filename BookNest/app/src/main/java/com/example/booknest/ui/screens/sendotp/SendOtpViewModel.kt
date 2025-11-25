package com.example.booknest.ui.screens.sendotp

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit

data class SendOtpUiState(
    val fullName:String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val verificationId: String? = null
)

class SendOtpViewModel: ViewModel(){
    private val _uiState = MutableStateFlow(SendOtpUiState())
    val uiState = _uiState.asStateFlow()

    fun onFullNameChange(fullName: String){
        _uiState.update {
            it.copy(fullName = fullName)
        }
    }

    fun onEmailChange(email: String){
        _uiState.update {
            it.copy(email=email)
        }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.update {
            it.copy(phoneNumber = phoneNumber)
        }
    }

    fun sendOtp(activity: Activity){
        val phoneNumber = "+91${_uiState.value.phoneNumber}"

        if (_uiState.value.phoneNumber.length != 10) {
            _uiState.update { it.copy(error = "Invalid phone number") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        val options = com.google.firebase.auth.PhoneAuthOptions.newBuilder(com.google.firebase.auth.FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    _uiState.update { it.copy(isLoading = false) }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _uiState.update { it.copy(isLoading = false, error = "Verification failed: ${e.message}") }
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            verificationId = verificationId
                        )
                    }
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun onNavigationDone() {
        _uiState.update { it.copy(verificationId = null) }
    }

}