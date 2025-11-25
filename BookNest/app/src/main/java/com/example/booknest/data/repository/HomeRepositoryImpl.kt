package com.example.booknest.data.repository

import com.example.booknest.data.model.Hotel
import com.example.booknest.data.model.Place
import com.example.booknest.data.model.PlaceDetail
import com.example.booknest.util.Resource
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : HomeRepository {

    override suspend fun getRecommendedPlaces(): Resource<List<Place>> {
        return try {
            val places = firebaseDatabase.getReference("recommended_places")
                .get()
                .await()
                .children
                .mapNotNull { it.getValue(Place::class.java) }
            Resource.Success(places)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getCities(): Resource<List<String>> {
        return try {
            val cities = firebaseDatabase.getReference("cities")
                .get()
                .await()
                .children
                .mapNotNull { it.getValue(String::class.java) }
            Resource.Success(cities)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getHotels(city: String): Resource<List<Hotel>> {
        return try {
            val hotels = firebaseDatabase.getReference("hotels").child(city)
                .get()
                .await()
                .children
                .mapNotNull { dataSnapshot ->
                    val hotel = dataSnapshot.getValue(Hotel::class.java)
                    hotel?.copy(id = dataSnapshot.key ?: "")
                }
            Resource.Success(hotels)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getHotelDetails(city: String, hotelId: String): Resource<Hotel> {
        return try {
            val hotel = firebaseDatabase.getReference("hotels").child(city).child(hotelId)
                .get()
                .await()
                .getValue(Hotel::class.java)
            if (hotel !=null){
                Resource.Success(hotel.copy(id = hotelId))
            }else{
                Resource.Error("Hotel not found")

            }
        }catch (e: Exception){
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getPlaceDetails(placeName: String): Resource<PlaceDetail> {
        return try{

            val details =firebaseDatabase.getReference("places_details").child(placeName)
                .get()
                .await()
                .getValue(PlaceDetail::class.java)

            if (details!=null){
                Resource.Success(details)
            }else{
                Resource.Error("Place details not found")
            }

        }catch (e: Exception){
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}