package com.faigenbloom.familybudget.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.domain.currency.GetAllCurrenciesUseCase
import com.faigenbloom.familybudget.domain.currency.GetSettingsUseCase
import com.faigenbloom.familybudget.domain.currency.SaveSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

class SettingsPageViewModel(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase,
) : ViewModel() {

    private fun onNameChanged(name: String) {
        _stateFlow.update { state ->
            state.copy(
                name = name,
                canSave = true,
            )
        }
    }

    private fun onSurnameChanged(surname: String) {
        _stateFlow.update { state ->
            state.copy(
                surname = surname,
                canSave = true,
            )
        }
    }

    private fun onNotificationsCheckChanged(isEnabled: Boolean) {
        _stateFlow.update { state ->
            state.copy(
                isNotificationsEnabled = isEnabled,
                canSave = true,
            )
        }
    }

    private fun onPasswordCheckChanged(isEnabled: Boolean) {
        _stateFlow.update { state ->
            state.copy(
                isPasswordEnabled = isEnabled,
                canSave = true,
            )
        }
    }

    private fun onCurrenciesDialogVisibilityChanged() {
        _stateFlow.update { state ->
            state.copy(
                isCurrenciesDialogVisible = true,
            )
        }
    }

    private fun onCurrencyChanged(currency: Currency) {
        _stateFlow.update { state ->
            state.copy(
                chosenCurrency = currency,
                isCurrenciesDialogVisible = false,
                canSave = true,
            )
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            saveSettingsUseCase(
                currency = state.chosenCurrency,
                isNotificationsEnabled = state.isNotificationsEnabled,
                isPasswordEnabled = state.isPasswordEnabled,
                name = state.name,
                familyName = state.surname,
            )
            _stateFlow.update { state ->
                state.copy(
                    canSave = false,
                )
            }
        }
    }

    private val state: SettingsState
        get() = _stateFlow.value

    private val _stateFlow = MutableStateFlow(
        SettingsState(
            onNameChanged = ::onNameChanged,
            onSurnameChanged = ::onSurnameChanged,
            onShowCurrencyDialog = ::onCurrenciesDialogVisibilityChanged,
            onNotificationsCheckChanged = ::onNotificationsCheckChanged,
            onPasswordCheckChanged = ::onPasswordCheckChanged,
            onCurrencyChanged = ::onCurrencyChanged,
            onSave = ::onSave,
        ),
    )

    val stateFlow = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = getSettingsUseCase()

            _stateFlow.update { state ->
                state.copy(
                    chosenCurrency = settings.currency,
                    currenciesList = getAllCurrenciesUseCase(settings.currency),
                    isNotificationsEnabled = settings.isNotificationsEnabled,
                    isPasswordEnabled = settings.isPasswordEnabled,
                    name = settings.name,
                    surname = settings.familyName,

                    )
            }
        }
    }
}

data class SettingsState(
    val currenciesList: List<Currency> = emptyList(),
    val name: String = "",
    val surname: String = "",
    val canSave: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
    val isPasswordEnabled: Boolean = false,
    val isCurrenciesDialogVisible: Boolean = false,
    val chosenCurrency: Currency = Currency.getInstance(Locale.getDefault()),
    val onNameChanged: (String) -> Unit,
    val onSurnameChanged: (String) -> Unit,
    val onShowCurrencyDialog: () -> Unit,
    val onNotificationsCheckChanged: (Boolean) -> Unit,
    val onPasswordCheckChanged: (Boolean) -> Unit,
    val onCurrencyChanged: (currency: Currency) -> Unit,
    val onSave: () -> Unit,
)
