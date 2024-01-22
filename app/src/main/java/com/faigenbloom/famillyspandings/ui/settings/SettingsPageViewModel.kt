package com.faigenbloom.famillyspandings.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.domain.GetChosenCurrencyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

class SettingsPageViewModel(
    repository: SettingsPageRepository,
    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,
) : ViewModel() {
    private var name: String = ""
    private var surname: String = ""
    private var chosenCurrency: Currency = Currency.getInstance(Locale.getDefault())
    private var isNotificationsEnabled: Boolean = true
    private val currenciesList: List<Currency> = repository.getAllCurrencies()
    private var isCurrenciesDropdownVisible: Boolean = false

    private val onNameChanged: (String) -> Unit = {
        name = it
        updateUi()
    }

    private val onSurnameChanged: (String) -> Unit = {
        surname = it
        updateUi()
    }
    private val onNotificationsCheckChanged: ((Boolean) -> Unit) = {
        isNotificationsEnabled = it
        updateUi()
    }
    private val onCurrenciesDropdownVisibilityChanged: (Boolean) -> Unit = {
        isCurrenciesDropdownVisible = it
        updateUi()
    }
    private val onCurrencyChanged: (currency: Currency) -> Unit = {
        chosenCurrency = it
        updateUi()
    }
    private val state: SettingsState
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

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow().apply {
        viewModelScope.launch {
            chosenCurrency = getChosenCurrencyUseCase()
            updateUi()
        }
    }

    private fun updateUi() = _stateFlow.update { state }
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
