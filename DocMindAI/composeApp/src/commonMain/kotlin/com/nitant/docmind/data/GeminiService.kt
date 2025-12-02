package com.nitant.docmind.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class GeminiService (
    private val client: HttpClient,
    private val apiKey: String
){
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    suspend fun analyzeDocument(extractedText:String): AiResult{

        val prompt = """
            Analyze the following text from a scanned document. 
            Identify the Title, the Type (Invoice, Receipt, Note, or Other), and a short 3-bullet-point Summary.
            
            Return ONLY a valid JSON object with no markdown formatting. The JSON must match this structure:
            {
              "title": "String",
              "type": "String",
              "summary": "String (Do NOT use an array. Use a single string with newlines for bullet points)"
            }
            
            Here is the text:
            $extractedText
        """.trimIndent()

        val requestBody = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt))))
        )

        // 1. Send Request to Google
        val response: GeminiResponse = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body()

        // 2. Extract the text answer
        val rawAnswer = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("AI returned no results")

        // 3. Clean up the answer
        val cleanJson = rawAnswer.replace("```json", "").replace("```", "").trim()

        return json.decodeFromString<AiResult>(cleanJson)
    }
}