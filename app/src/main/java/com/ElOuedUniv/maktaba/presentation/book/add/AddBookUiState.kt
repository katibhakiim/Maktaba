package com.ElOuedUniv.maktaba.presentation.book.add

import android.net.Uri

/**
 * TP5 – Bonus Task 4.1 / 4.2
 * Added [selectedImageUri] for the picked cover image and
 * [isUploading] to show progress during Supabase Storage upload.
 */
data class AddBookUiState(
    val title: String = "",
    val isbn: String = "",
    val nbPages: String = "",
    val isLoading: Boolean = false,
    val isUploading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFormValid: Boolean = false,
    val titleError: String? = null,
    val isbnError: String? = null,
    val nbPagesError: String? = null,
    val errorMessage: String? = null,
    val selectedImageUri: Uri? = null
)
