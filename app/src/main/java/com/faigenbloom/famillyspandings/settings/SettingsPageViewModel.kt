package com.faigenbloom.famillyspandings.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Currency

class SettingsPageViewModel(
    repository: SettingsPageRepository,
) : ViewModel() {
    private var name: String = ""
    private var surname: String = ""
    private var chosenCurrency: Currency = repository.getChosenCurrency()
    private var isNotificationsEnabled: Boolean = true
    private val currenciesList: List<Currency> = repository.getAllCurrencies()
    private var isCurrenciesDropdownVisible: Boolean = false

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
    private val onCurrenciesDropdownVisibilityChanged: (Boolean) -> Unit = {
        isCurrenciesDropdownVisible = it
        _budgetStateFlow.update { budgetState }
    }
    private val onCurrencyChanged: (currency: Currency) -> Unit = {
        chosenCurrency = it
        _budgetStateFlow.update { budgetState }
    }
    private val budgetState: SettingsState
        get() = SettingsState(
            name = name,
            surname = surname,
            onNameChanged = onNameChanged,
            onSurnameChanged = onSurnameChanged,
            chosenCurrency = chosenCurrency,
            isNotificationsEnabled = isNotificationsEnabled,
            currenciesList = currenciesList,
            isCurrenciesDropdownVisible = isCurrenciesDropdownVisible,
            onCurrenciesDropdownVisibilityChanged = onCurrenciesDropdownVisibilityChanged,
            onNotificationsCheckChanged = onNotificationsCheckChanged,
            onCurrencyChanged = onCurrencyChanged,
        )

    private val _budgetStateFlow = MutableStateFlow(budgetState)
    val budgetStateFlow = _budgetStateFlow.asStateFlow()
}

data class SettingsState(
    val name: String,
    val surname: String,
    val currenciesList: List<Currency>,
    val chosenCurrency: Currency,
    val onNameChanged: (String) -> Unit,
    val onSurnameChanged: (String) -> Unit,
    val isCurrenciesDropdownVisible: Boolean,
    val onCurrenciesDropdownVisibilityChanged: ((Boolean) -> Unit),
    val isNotificationsEnabled: Boolean,
    val onNotificationsCheckChanged: ((Boolean) -> Unit),
    val onCurrencyChanged: (currency: Currency) -> Unit,
)
