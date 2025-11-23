package com.example.chitchat.feature_auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.feature_auth.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.chitchat.core.domain.model.Result

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel(){

    var state by mutableStateOf(LoginState())

    fun onLoginClick(email:String,password:String){
        viewModelScope.launch {
            state = state.copy(isLoading = true, loginError = null)
            state = when (val result = loginUseCase(email, password)) {
                is Result.Success -> state.copy(isLoading = false, isLoginSuccess = true)
                is Result.Error -> state.copy(isLoading = false, loginError = result.exception.message)
            }
        }
    }
}