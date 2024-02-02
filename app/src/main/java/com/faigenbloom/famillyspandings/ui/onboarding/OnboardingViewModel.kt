package com.faigenbloom.famillyspandings.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.domain.family.GetFamilyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val getFamilyUseCase: GetFamilyUseCase,
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
            val familyData = getFamilyUseCase()
            if (familyData.id.isNotBlank()) {
                onAuthPassed()
            }
        }
    }
}

data class OnboardingState(val isScanQrClicked: () -> Unit)
