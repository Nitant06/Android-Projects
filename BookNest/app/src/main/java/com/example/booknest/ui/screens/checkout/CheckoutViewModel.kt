package com.example.booknest.ui.screens.checkout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknest.data.model.Hotel
import com.example.booknest.data.model.Room
import com.example.booknest.data.repository.AuthRepository
import com.example.booknest.data.repository.HomeRepository
import com.example.booknest.util.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class UserDetails(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null
)

data class CheckoutUiState(
    val hotel: Hotel? = null,
    val userDetails: UserDetails = UserDetails(),
    val selectedRooms: List<Room> = emptyList(),
    val roomPrice: Double = 0.0,
    val gst: Double = 0.0,
    val totalAmount: Double = 0.0,
    val numberOfDays: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState = _uiState.asStateFlow()

    private val city: String = requireNotNull(savedStateHandle["city"])
    private val hotelId: String = requireNotNull(savedStateHandle["hotelId"])
    private val roomsJson: String = requireNotNull(savedStateHandle["roomsJson"])

    init {
        val days: Int = requireNotNull(savedStateHandle["numberOfDays"])
        _uiState.update { it.copy(numberOfDays = days) }

        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val userId = firebaseAuth.currentUser?.uid
            val userDetailsFromDb = if (userId != null) {
                authRepository.getUserDetails(userId)
            } else {
                UserDetails()
            }

            val selectedRoomsList = Json.decodeFromString<List<Room>>(roomsJson)

            when (val result = repository.getHotelDetails(city, hotelId)) {
                is Resource.Success -> {
                    val hotel = result.data!!
                    val prices = calculatePrices(selectedRoomsList)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hotel = hotel,
                            selectedRooms = selectedRoomsList,
                            roomPrice = prices.first,
                            gst = prices.second,
                            totalAmount = prices.third,
                            userDetails = userDetailsFromDb
                        )
                    }
                }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun onRemoveRoom(room: Room) {
        val newSelectedRooms = _uiState.value.selectedRooms.toMutableList()
        newSelectedRooms.remove(room)

        val prices = calculatePrices(newSelectedRooms)

        _uiState.update {
            it.copy(
                selectedRooms = newSelectedRooms,
                roomPrice = prices.first,
                gst = prices.second,
                totalAmount = prices.third
            )
        }
    }

    private fun calculatePrices(selectedRooms: List<Room>): Triple<Double, Double, Double> {
        val cost = selectedRooms.sumOf { it.price }
        val currentDays = _uiState.value.numberOfDays
        val finalRoomPrice = (cost * currentDays).toDouble()
        val finalGst = finalRoomPrice * 0.18
        val finalTotal = finalRoomPrice + finalGst

        return Triple(finalRoomPrice, finalGst, finalTotal)
    }
}