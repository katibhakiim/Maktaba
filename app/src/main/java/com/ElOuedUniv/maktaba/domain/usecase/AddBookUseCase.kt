package com.ElOuedUniv.maktaba.domain.usecase

import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.data.repository.BookRepository
import javax.inject.Inject

class AddBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    // Exercise 2: Call the repository to add the book
    operator fun invoke(book: Book) {
        bookRepository.addBook(book)
    }
}
