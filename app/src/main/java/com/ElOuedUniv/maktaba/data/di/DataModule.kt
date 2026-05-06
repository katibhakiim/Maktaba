package com.ElOuedUniv.maktaba.data.di

import com.ElOuedUniv.maktaba.data.repository.BookRepository
import com.ElOuedUniv.maktaba.data.repository.CategoryRepository
import com.ElOuedUniv.maktaba.data.repository.SupabaseBookRepositoryImpl
import com.ElOuedUniv.maktaba.data.repository.SupabaseCategoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

/**
 * TP5 - Task 2.2
 * Updated DataModule: now provides Supabase-backed implementations instead of
 * the old in-memory ones.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(
        supabaseClient: SupabaseClient
    ): CategoryRepository {
        return SupabaseCategoryRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideBookRepository(
        supabaseClient: SupabaseClient
    ): BookRepository {
        return SupabaseBookRepositoryImpl(supabaseClient)
    }
}
