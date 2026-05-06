package com.ElOuedUniv.maktaba.presentation.navigation

import androidx.lifecycle.ViewModel
import com.ElOuedUniv.maktaba.data.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * TP5 – Task 3.2
 * Provides the [hasCompletedOnboarding] flow to [NavGraph] so the correct
 * start destination can be selected without blocking the UI thread.
 */
@HiltViewModel
class NavGraphViewModel @Inject constructor(
    onboardingRepository: OnboardingRepository
) : ViewModel() {

    /** Emits `true` when the user has previously completed onboarding. */
    val hasCompletedOnboarding: Flow<Boolean> = onboardingRepository.hasCompletedOnboarding
}
