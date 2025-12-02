package com.nitant.docmind.domain.repository

import com.nitant.docmind.domain.models.Document
import kotlinx.coroutines.flow.Flow

interface DocRepository{
    fun getAllDocuments(): Flow<List<Document>>
    suspend fun analyzeAndSaveDocument(text: String, imagePath: String): Result<Document>
    suspend fun deleteDocument(document: Document)
}