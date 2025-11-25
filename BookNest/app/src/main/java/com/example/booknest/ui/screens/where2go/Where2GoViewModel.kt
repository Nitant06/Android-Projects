package com.example.booknest.ui.screens.where2go

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknest.data.model.Place
import com.example.booknest.data.repository.HomeRepository
import com.example.booknest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Where2GoUiState(
    val places: List<Place> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class Where2GoViewModel @Inject constructor(
    private val repository: HomeRepository
): ViewModel(){

    private val _uiState = MutableStateFlow(Where2GoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchPlaces()
    }

    private fun fetchPlaces(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = repository.getRecommendedPlaces()){
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, places = result.data?:emptyList()) }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {
                    _uiState.update { it.copy(isLoading = false, error = "Unknown error occurred") }
                }
            }
        }
    }

}


