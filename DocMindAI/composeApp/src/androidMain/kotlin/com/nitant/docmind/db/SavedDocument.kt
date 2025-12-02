package com.nitant.docmind.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class SavedDocument(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val type: String,
    val summary: String,
    val imagePath: String,
    val timestamp: Long = System.currentTimeMillis()
)