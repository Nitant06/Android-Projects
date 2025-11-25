package com.example.booknest.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.booknest.util.Constants

class SplashViewModel: ViewModel(){

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _startDestination = MutableStateFlow("")
    val startDestination: StateFlow<String> = _startDestination

    init {
        viewModelScope.launch {
            delay(3000)
            val user = FirebaseAuth.getInstance().currentUser

            if (user!=null){
                _startDestination.value = Constants.FIND_ROOM_ROUTE
            }else{
                _startDestination.value = Constants.SEND_OTP_ROUTE
            }

            _isLoading.value = false
        }
    }

}