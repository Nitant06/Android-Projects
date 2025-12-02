package com.nitant.docmind.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val id: Int,
    val title: String,
    val summary: String,
    val type: DocType,
    val timestamp: Long,
    val imagePath: String
)

@Serializable
enum class DocType{
    INVOICE,
    RECEIPT,
    NOTE,
    OTHER
}