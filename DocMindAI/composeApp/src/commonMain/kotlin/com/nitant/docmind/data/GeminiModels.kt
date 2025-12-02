package com.nitant.docmind.data

import kotlinx.serialization.Serializable

// Request Gemini

@Serializable
data class GeminiRequest(
    val contents: List<Content>
)

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String
)

// Response Gemini

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(
    val content: Content? = null
)

@Serializable
data class AiResult(
    val title: String,
    val type: String,
    val summary: String
)