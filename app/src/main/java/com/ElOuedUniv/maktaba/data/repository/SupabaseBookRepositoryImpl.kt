package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * TP5 – Task 2.1
 * Supabase-backed implementation of [BookRepository].
 * Fetches and inserts books using the Supabase Postgrest plugin.
 *
 * Expected Supabase table schema (books):
 *   isbn      TEXT PRIMARY KEY
 *   title     TEXT NOT NULL
 *   nb_pages  INT  NOT NULL
 *   image_url TEXT
 */
class SupabaseBookRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : BookRepository {

    private val table = "books"

    /** Fetches all rows from the `books` table and emits them as a [List]. */
    override fun getAllBooks(): Flow<List<Book>> = flow {
        val books = supabaseClient
            .postgrest[table]
            .select()
            .decodeList<Book>()
        emit(books)
    }

    /**
     * Returns a single [Book] by [isbn].
     * Note: the interface contract is non-suspend; callers that need
     * reactive access should use [getAllBooks] and filter locally.
     */
    override fun getBookByIsbn(isbn: String): Book? {
        return runBlocking {
            supabaseClient
                .postgrest[table]
                .select {
                    filter { eq("isbn", isbn) }
                }
                .decodeList<Book>()
                .firstOrNull()
        }
    }

    /**
     * Inserts a new [Book] row into the `books` table.
     *
     * [addBook] is called from [AddBookViewModel.addBook] which already runs
     * inside a `viewModelScope` coroutine, so [runBlocking] here is safe for
     * this academic project scope.
     */
    override fun addBook(book: Book) {
        runBlocking {
            supabaseClient
                .postgrest[table]
                .insert(book)
        }
    }
}
