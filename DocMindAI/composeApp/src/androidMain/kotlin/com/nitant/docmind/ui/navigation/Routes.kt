package com.nitant.docmind.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
object CameraRoute

@Serializable
data class ResultRoute(
    val imagePath: String
)

@Serializable
data class DetailRoute(
    val title: String,
    val type: String,
    val summary: String,
    val imagePath: String,
    val timestamp: Long,
    val id: Int
)