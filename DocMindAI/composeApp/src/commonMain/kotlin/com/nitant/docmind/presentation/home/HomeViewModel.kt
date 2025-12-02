package com.nitant.docmind.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitant.docmind.domain.models.Document
import com.nitant.docmind.domain.repository.DocRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DocRepository) : ViewModel() {

    val documents: StateFlow<List<Document>> = repository.getAllDocuments()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteDocument(doc: Document) {
        viewModelScope.launch {
            repository.deleteDocument(doc)
        }
    }
}