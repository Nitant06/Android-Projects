package com.example.booknest.ui.screens.placeDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknest.data.model.PlaceDetail
import com.example.booknest.data.repository.HomeRepository
import com.example.booknest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlaceDetailsUiState(
    val place: PlaceDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(
    private val repository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val placeName: String = requireNotNull(savedStateHandle["placeName"])
        fetchPlaceDetails(placeName)
    }

    private fun fetchPlaceDetails(name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.getPlaceDetails(name)) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, place = result.data) }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }
}