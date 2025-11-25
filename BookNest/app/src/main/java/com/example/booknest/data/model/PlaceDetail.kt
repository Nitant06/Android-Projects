package com.example.booknest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaceDetail(
    val name:String = "",
    val imageUrl: String = "",
    val description: String = ""
)