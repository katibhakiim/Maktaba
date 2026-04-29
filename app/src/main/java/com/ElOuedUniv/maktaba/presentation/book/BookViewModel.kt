package com.ElOuedUniv.maktaba.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import com.ElOuedUniv.maktaba.domain.usecase.GetBooksUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {

    // Exercise 1: Single MutableStateFlow<BookUiState> instead of multiple separate flows
    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    // Exercise 3: One-time UI events using SharedFlow
    private val _uiEvent = MutableSharedFlow<BookUiEvent>()
    val uiEvent: SharedFlow<BookUiEvent> = _uiEvent.asSharedFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            // Exercise 1: Update loading state using .copy()
            _uiState.update { it.copy(isLoading = true) }
            getBooksUseCase()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Unknown error"
                        )
                    }
                    // Exercise 3: Send a one-time error event
                    _uiEvent.emit(BookUiEvent.ShowSnackbar("Failed to load books: ${e.message}"))
                }
                .collect { bookList ->
                    _uiState.update {
                        it.copy(
                            books = bookList,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    /**
     * Exercise 3: Handle UI Actions using a when expression
     */
    fun onAction(action: BookUiAction) {
        when (action) {
            BookUiAction.RefreshBooks -> refreshBooks()

            BookUiAction.OnAddBookClick -> {
                // Set isAddingBook = true to show the dialog
                _uiState.update { it.copy(isAddingBook = true) }
            }

            BookUiAction.OnDismissAddBook -> {
                // Set isAddingBook = false to hide the dialog
                _uiState.update { it.copy(isAddingBook = false) }
            }

            is BookUiAction.OnAddBookConfirm -> {
                // Create the book, call AddBookUseCase, and hide the dialog
                val newBook = Book(
                    title = action.title,
                    isbn = action.isbn,
                    nbPages = action.nbPages
                )
                addBookUseCase(newBook)
                _uiState.update { it.copy(isAddingBook = false) }

                // Send a one-time snackbar event to confirm the action
                viewModelScope.launch {
                    _uiEvent.emit(BookUiEvent.ShowSnackbar("Book \"${action.title}\" added successfully!"))
                }
            }
        }
    }

    fun refreshBooks() {
        loadBooks()
    }
}
