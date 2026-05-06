package com.ElOuedUniv.maktaba.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * TP5 - Task 3.1
 * Marks onboarding as complete by persisting the flag via [OnboardingRepository].
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    /** Called when the user taps "Get Started". Writes the flag to DataStore. */
    fun onCompleteOnboarding() {
        viewModelScope.launch {
            onboardingRepository.setOnboardingCompleted()
        }
    }
}
