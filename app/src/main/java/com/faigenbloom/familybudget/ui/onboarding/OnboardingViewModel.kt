package com.faigenbloom.familybudget.ui.onboarding

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
    private fun isScanQrClicked() {

    }

    private val _stateFlow = MutableStateFlow(
        OnboardingState(
            isScanQrClicked = ::isScanQrClicked,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow().apply {
        viewModelScope.launch {
            if (loadAllDataUseCase()) {
                onAuthPassed()
            }
        }
    }
}

data class OnboardingState(val isScanQrClicked: () -> Unit)
