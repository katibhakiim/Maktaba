package com.ElOuedUniv.maktaba.presentation.book.add

import androidx.lifecycle.ViewModel
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AddBookUiAction) {
        when (action) {
            is AddBookUiAction.OnTitleChange -> {
                _uiState.update { it.copy(title = action.title) }
            }
            is AddBookUiAction.OnIsbnChange -> {
                _uiState.update { it.copy(isbn = action.isbn) }
            }
            is AddBookUiAction.OnPagesChange -> {
                _uiState.update { it.copy(nbPages = action.pages) }
            }
            AddBookUiAction.OnAddClick -> {
                addBook()
            }
        }
        if (action !is AddBookUiAction.OnAddClick) {
            validateInputs()
        }
    }

    private fun validateInputs() {
        val titleValid = _uiState.value.title.isNotBlank()
        val isbnValid = _uiState.value.isbn.matches(Regex("\\d{13}"))
        val nbPagesValid = _uiState.value.nbPages.toIntOrNull()?.let { it > 0 } ?: false

        _uiState.update {
            it.copy(
                titleError = if (titleValid || it.title.isEmpty()) null else "Title cannot be empty",
                isbnError = if (isbnValid || it.isbn.isEmpty()) null else "ISBN must be exactly 13 digits",
                nbPagesError = if (nbPagesValid || it.nbPages.isEmpty()) null else "Pages must be a positive number",
                isFormValid = titleValid && isbnValid && nbPagesValid
            )
        }
    }

    private fun addBook() {
        val currentState = _uiState.value
        val book = Book(
            isbn = currentState.isbn,
            title = currentState.title,
            nbPages = currentState.nbPages.toIntOrNull() ?: 0
        )
        addBookUseCase(book)
        _uiState.update { it.copy(isSuccess = true) }
    }
}
