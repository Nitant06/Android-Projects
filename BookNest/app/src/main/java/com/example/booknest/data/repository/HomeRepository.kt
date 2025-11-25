package com.example.booknest.data.repository

import com.example.booknest.data.model.Hotel
import com.example.booknest.data.model.Place
import com.example.booknest.data.model.PlaceDetail
import com.example.booknest.util.Resource

interface HomeRepository {
    suspend fun getRecommendedPlaces(): Resource<List<Place>>
    suspend fun getCities(): Resource<List<String>>
    suspend fun getHotels(city: String): Resource<List<Hotel>>
    suspend fun getHotelDetails(city: String,hotelId: String): Resource<Hotel>
    suspend fun getPlaceDetails(placeName: String): Resource<PlaceDetail>
}