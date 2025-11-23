package com.example.chitchat.feature_auth.presentation.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_auth.domain.use_case.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase
): ViewModel() {

    var state by mutableStateOf(SignupState())
        private set

    fun onSignupClick(email: String,password: String){
        viewModelScope.launch {
                state = state.copy(isLoading = true, signupError = null)

                val result = signupUseCase(email,password)

                state = when(result){
                    is Result.Success ->{
                        state.copy(isLoading = false, isSignupSuccess = true)
                    }
                    is Result.Error ->{
                        state.copy(isLoading = false, signupError = result.exception.message)
                    }
                }
        }
    }
}