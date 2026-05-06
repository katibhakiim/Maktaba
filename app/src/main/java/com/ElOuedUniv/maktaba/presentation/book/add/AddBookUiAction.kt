package com.ElOuedUniv.maktaba.presentation.book.add

import android.net.Uri

/**
 * TP5 – Bonus Task 4.1
 * Added [OnImageSelected] to carry the content URI of the picked image.
 */
sealed class AddBookUiAction {
    data class OnTitleChange(val title: String) : AddBookUiAction()
    data class OnIsbnChange(val isbn: String)   : AddBookUiAction()
    data class OnPagesChange(val pages: String) : AddBookUiAction()
    data class OnImageSelected(val uri: Uri)    : AddBookUiAction()
    object OnAddClick : AddBookUiAction()
}
