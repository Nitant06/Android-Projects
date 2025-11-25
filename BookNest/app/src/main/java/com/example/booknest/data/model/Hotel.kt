package com.example.booknest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val type: String = "",
    val price: Long = 0,
    val isAvailable: Boolean = true
)

@Serializable
data class Hotel(
    val id: String = "",
    val name: String = "",
    val about: String = "",
    val rating: Double = 0.0,
    val priceRange: String = "",
    val imageUrl: String = "",
    val amenities: List<String> = emptyList(),
    val propertyRules: List<String> = emptyList(),
    val rooms: List<Room> = emptyList()
)