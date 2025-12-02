package com.nitant.docmind.data.repository


import com.nitant.docmind.data.GeminiService
import com.nitant.docmind.db.DocumentDao
import com.nitant.docmind.db.SavedDocument
import com.nitant.docmind.domain.models.DocType
import com.nitant.docmind.domain.models.Document
import com.nitant.docmind.domain.repository.DocRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DocRepositoryImpl(
    private val dao: DocumentDao,
    private val aiService: GeminiService
) : DocRepository {

    override fun getAllDocuments(): Flow<List<Document>> {
        return dao.getAllDocuments().map { entities ->
            entities.map { entity ->
                Document(
                    id = entity.id,
                    title = entity.title,
                    type = try {
                        DocType.valueOf(entity.type)
                    } catch (_: Exception) {
                        DocType.OTHER
                    },
                    summary = entity.summary,
                    imagePath = entity.imagePath,
                    timestamp = entity.timestamp,
                )
            }
        }
    }

    override suspend fun analyzeAndSaveDocument(text: String, imagePath: String): Result<Document> {
        return try {
            val aiResult = aiService.analyzeDocument(text)

            // Save to DB
            val entity = SavedDocument(
                title = aiResult.title,
                type = aiResult.type.uppercase(),
                summary = aiResult.summary,
                imagePath = imagePath,
                timestamp = System.currentTimeMillis()
            )
            dao.insert(entity)

            // Return Domain Model
            Result.success(
                Document(
                    id=0,
                    title=entity.title,
                    summary = entity.summary,
                    type = try {
                        DocType.valueOf(entity.type)
                    } catch(_: Exception) {
                        DocType.OTHER },
                    timestamp = entity.timestamp,
                    imagePath=entity.imagePath,
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteDocument(document: Document) {
        val entity = SavedDocument(
            id = document.id,
            title = document.title,
            type = document.type.name,
            summary = document.summary,
            imagePath = document.imagePath,
            timestamp = document.timestamp
        )
        dao.delete(entity)
    }
}