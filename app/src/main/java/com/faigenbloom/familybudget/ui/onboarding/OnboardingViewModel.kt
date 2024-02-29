package com.faigenbloom.familybudget.ui.onboarding

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.domain.auth.LoadAllDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val loadAllDataUseCase: LoadAllDataUseCase,
) : ViewModel() {
    var onAuthPassed: () -> Unit = {}
    private val state: OnboardingState
        get() = _stateFlow.value
    private val _stateFlow = MutableStateFlow(OnboardingState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            if (loadAllDataUseCase()) {
                onAuthPassed()
            } else {
                state.isLoading.value = false
            }
        }
    }
}

data class OnboardingState(
    val isLoading: MutableState<Boolean> = mutableStateOf(true),
)
