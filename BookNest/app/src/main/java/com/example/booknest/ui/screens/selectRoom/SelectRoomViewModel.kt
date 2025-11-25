package com.example.booknest.ui.screens.selectRoom


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknest.data.model.Hotel
import com.example.booknest.data.model.Room
import com.example.booknest.data.repository.HomeRepository
import com.example.booknest.util.Constants
import com.example.booknest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

data class SelectRoomUiState(
    val hotel: Hotel? = null,
    val selectedRooms: List<Room> = emptyList(),
    val totalCost: Long = 0,
    val numberOfDays: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SelectRoomViewModel @Inject constructor(
    private val repository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectRoomUiState())
    val uiState = _uiState.asStateFlow()

    private val city: String = requireNotNull(savedStateHandle["city"])
    private val hotelId: String = requireNotNull(savedStateHandle["hotelId"])

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        val days: Int = requireNotNull(savedStateHandle["numberOfDays"])
        _uiState.update { it.copy(numberOfDays = days) }

        fetchHotelDetails()
    }

    private fun fetchHotelDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.getHotelDetails(city, hotelId)) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, hotel = result.data) }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun onRoomSelectionChanged(room: Room, isSelected: Boolean) {
        val currentSelection = _uiState.value.selectedRooms.toMutableList()
        if (isSelected) {
            if (!currentSelection.contains(room)) {
                currentSelection.add(room)
            }
        } else {
            currentSelection.remove(room)
        }
        _uiState.update { it.copy(selectedRooms = currentSelection) }
        calculateTotalCost()
    }

    private fun calculateTotalCost() {
        var cost = 0L
        _uiState.value.selectedRooms.forEach { room ->
            cost += room.price
        }
        _uiState.update { it.copy(totalCost = cost * _uiState.value.numberOfDays) }
    }

    fun onCheckoutClicked() {
        viewModelScope.launch {
            val roomsJson = Json.encodeToString(_uiState.value.selectedRooms)
            val encodedRoomsJson = URLEncoder.encode(roomsJson, StandardCharsets.UTF_8.toString())
            val numberOfDays = _uiState.value.numberOfDays
            val route = "${Constants.CHECKOUT_ROUTE}/$city/$hotelId/$encodedRoomsJson/$numberOfDays"
            _navigationEvent.emit(route)
        }
    }
}