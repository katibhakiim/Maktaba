package com.ElOuedUniv.maktaba.presentation.book.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * TP5 – Task 2.1 / Bonus 4.2
 *
 * Handles form validation, image selection, Supabase Storage upload, and book insertion.
 * The entire add-book flow runs inside [viewModelScope] on [Dispatchers.IO] so that the
 * [runBlocking] inside [AddBookUseCase] / [SupabaseBookRepositoryImpl.addBook] never
 * blocks the main thread.
 */
@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase,
    private val supabaseClient: SupabaseClient,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState = _uiState.asStateFlow()

    // ── Actions ──────────────────────────────────────────────────────────────

    fun onAction(action: AddBookUiAction) {
        when (action) {
            is AddBookUiAction.OnTitleChange   -> { _uiState.update { it.copy(title = action.title) };  validateInputs() }
            is AddBookUiAction.OnIsbnChange    -> { _uiState.update { it.copy(isbn  = action.isbn)  };  validateInputs() }
            is AddBookUiAction.OnPagesChange   -> { _uiState.update { it.copy(nbPages = action.pages) }; validateInputs() }
            is AddBookUiAction.OnImageSelected -> { _uiState.update { it.copy(selectedImageUri = action.uri) } }
            AddBookUiAction.OnAddClick         -> { if (_uiState.value.isFormValid) addBook() }
        }
    }

    // ── Validation ────────────────────────────────────────────────────────────

    private fun validateInputs() {
        val title  = _uiState.value.title
        val isbn   = _uiState.value.isbn
        val pages  = _uiState.value.nbPages

        val titleError  = if (title.isBlank()) "Title cannot be empty" else null
        val isbnError   = if (isbn.length != 13 || isbn.any { !it.isDigit() }) "ISBN must be 13 digits" else null
        val pagesInt    = pages.toIntOrNull()
        val pagesError  = if (pagesInt == null || pagesInt <= 0) "Pages must be a positive number" else null

        _uiState.update {
            it.copy(
                titleError  = titleError,
                isbnError   = isbnError,
                nbPagesError = pagesError,
                isFormValid  = titleError == null && isbnError == null && pagesError == null
            )
        }
    }

    // ── Book creation ─────────────────────────────────────────────────────────

    private fun addBook() {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true, errorMessage = null) }
            try {
                // Bonus Task 4.2 – upload cover image to Supabase Storage
                val imageUrl: String? = withContext(Dispatchers.IO) {
                    _uiState.value.selectedImageUri?.let { uri -> uploadCover(uri) }
                }

                val state = _uiState.value
                val book  = Book(
                    isbn     = state.isbn,
                    title    = state.title,
                    nbPages  = state.nbPages.toIntOrNull() ?: 0,
                    imageUrl = imageUrl
                )

                withContext(Dispatchers.IO) {
                    addBookUseCase(book)          // internally calls SupabaseBookRepositoryImpl.addBook()
                }

                _uiState.update { it.copy(isUploading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isUploading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    /**
     * Bonus Task 4.2
     * Reads [uri] bytes from the content resolver, uploads them to the
     * `book_covers` bucket in Supabase Storage, and returns the public URL.
     * Must be called on [Dispatchers.IO].
     */
    private suspend fun uploadCover(uri: Uri): String {
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw IllegalStateException("Cannot read image from URI: $uri")

        val fileName = "covers/${_uiState.value.isbn}_${System.currentTimeMillis()}.jpg"
        val bucket   = supabaseClient.storage["book_covers"]
        bucket.upload(fileName, bytes) {
            upsert = true
        }
        return bucket.publicUrl(fileName)
    }
}
