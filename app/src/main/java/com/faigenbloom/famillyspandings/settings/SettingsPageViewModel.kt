package com.faigenbloom.famillyspandings.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsPageViewModel(
    private val repository: SettingsPageRepository,
) : ViewModel() {
    private var name: String = ""
    private var surname: String = ""
    private var isNotificationsEnabled: Boolean = true

    private val onNameChanged: (String) -> Unit = {
        name = it
        _budgetStateFlow.update { budgetState }
    }
    private val onSurnameChanged: (String) -> Unit = {
        surname = it
        _budgetStateFlow.update { budgetState }
    }
    private val onNotificationsCheckChanged: ((Boolean) -> Unit) = {
        isNotificationsEnabled = it
        _budgetStateFlow.update { budgetState }
    }
    private val budgetState: SettingsState
        get() = SettingsState(
            name = name,
            surname = surname,
            onNameChanged = onNameChanged,
            onSurnameChanged = onSurnameChanged,
            isNotificationsEnabled = isNotificationsEnabled,
            onNotificationsCheckChanged = onNotificationsCheckChanged,
        )

    private val _budgetStateFlow = MutableStateFlow(budgetState)
    val budgetStateFlow = _budgetStateFlow.asStateFlow()
}

data class SettingsState(
    val name: String,
    val surname: String,
    val onNameChanged: (String) -> Unit,
    val onSurnameChanged: (String) -> Unit,
    val isNotificationsEnabled: Boolean,
    val onNotificationsCheckChanged: ((Boolean) -> Unit),
)
