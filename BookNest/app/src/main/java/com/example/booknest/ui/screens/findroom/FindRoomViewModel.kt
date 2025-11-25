package com.example.booknest.ui.screens.findroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknest.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.booknest.data.model.Place
import com.example.booknest.data.repository.AuthRepository
import com.example.booknest.util.Constants
import com.example.booknest.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit


data class FindRoomUiState(
    val cities: List<String> = emptyList(),
    val recommendedPlaces: List<Place> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    val selectedCity: String = "",
    val checkInDate: String? = null,
    val checkOutDate: String? = null,
    val numberOfRooms: Int = 1
)

@HiltViewModel
class FindRoomViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FindRoomUiState())
    val uiState = _uiState.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadInitialData()
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutEvent.emit(Unit)
        }
    }

    fun onCitySelected(city: String) {
        _uiState.update { it.copy(selectedCity = city) }
    }

    fun onCheckInDateSelected(date: String) {
        _uiState.update { it.copy(checkInDate = date) }
    }

    fun onCheckOutDateSelected(date: String) {
        _uiState.update { it.copy(checkOutDate = date) }
    }

    fun onRoomCountChanged(count: Int) {
        if (count > 0) {
            _uiState.update { it.copy(numberOfRooms = count) }
        }
    }

    fun onSearchClicked() {
        viewModelScope.launch {
            val state = _uiState.value
            val city = state.selectedCity
            val checkIn = state.checkInDate
            val checkOut = state.checkOutDate


            if (city.isNotEmpty() && checkIn != null && checkOut != null) {
                val numberOfDays = calculateDaysBetween(checkIn, checkOut)

                if (numberOfDays > 0) {
                    _navigationEvent.emit("${Constants.SELECT_HOTEL_ROUTE}/$city/$numberOfDays")
                } else {
                    _uiState.update { it.copy(error = "Check-in and check-out dates are invalid") }
                }
            } else {
                _uiState.update { it.copy(error = "Please select a city") }
            }
        }
    }

    private fun calculateDaysBetween(startDateStr: String, endDateStr: String): Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val startDate = sdf.parse(startDateStr)
            val endDate = sdf.parse(endDateStr)
            val diff = endDate.time - startDate.time
            // Use TimeUnit for a reliable conversion
            TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(1)
        } catch (e: Exception) {
            0L // Return 0 or handle error if parsing fails
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val citiesResult = repository.getCities()
            when (citiesResult) {
                is Resource.Success -> _uiState.update { it.copy(cities = citiesResult.data ?: emptyList()) }
                is Resource.Error -> _uiState.update { it.copy(error = citiesResult.message) }
                else -> {}
            }

            val placesResult = repository.getRecommendedPlaces()
            when (placesResult) {
                is Resource.Success -> _uiState.update { it.copy(recommendedPlaces = placesResult.data ?: emptyList()) }
                is Resource.Error -> _uiState.update { it.copy(error = placesResult.message) }
                else -> {}
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }


}