package com.faigenbloom.familybudget.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.domain.currency.GetAllCurrenciesUseCase
import com.faigenbloom.familybudget.domain.currency.GetChosenCurrencyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

class SettingsPageViewModel(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,
) : ViewModel() {

    private fun onNameChanged(name: String) {
        _stateFlow.update { state ->
            state.copy(
                name = name,
            )
        }
    }

    private fun onSurnameChanged(surname: String) {
        _stateFlow.update { state ->
            state.copy(
                surname = surname,
            )
        }
    }

    private fun onNotificationsCheckChanged(isEnabled: Boolean) {
        _stateFlow.update { state ->
            state.copy(
                isNotificationsEnabled = isEnabled,
            )
        }
    }

    private fun onCurrenciesDialogVisibilityChanged(isVisible: Boolean) {
        _stateFlow.update { state ->
            state.copy(
                isCurrenciesDialogVisible = isVisible,
            )
        }
    }

    private fun onCurrencyChanged(currency: Currency) {
        _stateFlow.update { state ->
            state.copy(
                chosenCurrency = currency,
            )
        }
    }

    private val _stateFlow = MutableStateFlow(
        SettingsState(
            onNameChanged = ::onNameChanged,
            onSurnameChanged = ::onSurnameChanged,
            onCurrenciesDropdownVisibilityChanged = ::onCurrenciesDialogVisibilityChanged,
            onNotificationsCheckChanged = ::onNotificationsCheckChanged,
            onCurrencyChanged = ::onCurrencyChanged,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            _stateFlow.update { state ->
                state.copy(
                    chosenCurrency = getChosenCurrencyUseCase(),
                    currenciesList = getAllCurrenciesUseCase(),
                )
            }
        }
    }
}

data class SettingsState(
    val currenciesList: List<Currency> = emptyList(),
    val name: String = "",
    val surname: String = "",
    val isNotificationsEnabled: Boolean = true,
    val isCurrenciesDialogVisible: Boolean = false,
    val chosenCurrency: Currency = Currency.getInstance(Locale.getDefault()),
    val onNameChanged: (String) -> Unit,
    val onSurnameChanged: (String) -> Unit,
    val onCurrenciesDropdownVisibilityChanged: ((Boolean) -> Unit),
    val onNotificationsCheckChanged: ((Boolean) -> Unit),
    val onCurrencyChanged: (currency: Currency) -> Unit,
)
