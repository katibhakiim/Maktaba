package com.ElOuedUniv.maktaba.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Jetpack DataStore extension on [Context] — one instance per app. */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "maktaba_prefs")

/**
 * TP5 - Task 3.1
 * Persists the onboarding-completion flag using Jetpack DataStore Preferences.
 */
@Singleton
class OnboardingRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("hasCompletedOnboarding")
    }

    /** Emits `true` once the user has completed onboarding. */
    val hasCompletedOnboarding: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[HAS_COMPLETED_ONBOARDING] ?: false }

    /** Saves the flag so onboarding is skipped on next launch. */
    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { prefs ->
            prefs[HAS_COMPLETED_ONBOARDING] = true
        }
    }
}
