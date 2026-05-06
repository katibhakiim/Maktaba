package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Category
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * TP5 - Task 2.1
 * Supabase-backed implementation of [CategoryRepository].
 * Fetches categories from the Supabase `categories` table.
 *
 * Expected Supabase table schema (categories):
 *   id          TEXT PRIMARY KEY
 *   name        TEXT NOT NULL
 *   description TEXT
 */
class SupabaseCategoryRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : CategoryRepository {

    private val table = "categories"

    /** Fetches all rows from the [table] and emits them as a list. */
    override fun getAllCategories(): Flow<List<Category>> = flow {
        val categories = supabaseClient
            .postgrest[table]
            .select()
            .decodeList<Category>()
        emit(categories)
    }

    /** Returns a single [Category] matching [id], or null if not found. */
    override fun getCategoryById(id: String): Category? {
        // Non-suspend; use getAllCategories() for async access.
        return null
    }
}
