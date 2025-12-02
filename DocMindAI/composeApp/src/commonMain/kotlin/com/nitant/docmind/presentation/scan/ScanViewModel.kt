package com.nitant.docmind.presentation.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitant.docmind.domain.models.Document
import com.nitant.docmind.domain.repository.DocRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ScanUiState {
    object Idle : ScanUiState()
    object Loading : ScanUiState()
    data class Success(val document: Document) : ScanUiState()
    data class Error(val message: String) : ScanUiState()
}

class ScanViewModel(private val repository: DocRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ScanUiState>(ScanUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun analyzeImage(text: String, imagePath: String) {
        _uiState.value = ScanUiState.Loading

        viewModelScope.launch {
            val result = repository.analyzeAndSaveDocument(text, imagePath)

            result.onSuccess { doc ->
                _uiState.value = ScanUiState.Success(doc)
            }.onFailure { error ->
                _uiState.value = ScanUiState.Error(error.message ?: "Unknown Error")
            }
        }
    }

    fun resetState() {
        _uiState.value = ScanUiState.Idle
    }
}