package com.faigenbloom.familybudget.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.domain.auth.LoadAllDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val loadAllDataUseCase: LoadAllDataUseCase,
) : ViewModel() {
    var onAuthPassed: () -> Unit = {}
    private val _stateFlow = MutableStateFlow(OnboardingState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            if (loadAllDataUseCase()) {
                onAuthPassed()
            } else {
                _stateFlow.update { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }
}

data class OnboardingState(
    val isLoading: Boolean = true,
)
