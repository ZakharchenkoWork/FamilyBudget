package com.faigenbloom.familybudget.ui.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.domain.auth.LoginUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginPageViewModel(
    private val loginUserUseCase: LoginUserUseCase,
) : ViewModel() {
    var onLoggedIn: () -> Unit = {}
    val loginField = StateAble("baskinaerobins@gmail.com") { onDropError() }
    private fun onLoginClicked() {

        viewModelScope.launch {
            state.isLoading.value = true
            if (loginUserUseCase(loginField.state.value, state.passwordState.value)) {
                onLoggedIn()
            } else {
                _stateFlow.update {
                    state.copy(
                        authError = true,
                    )
                }
            }
            state.isLoading.value = false
        }
    }

    private fun onDropError() {
        _stateFlow.update {
            state.copy(
                authError = false,
            )
        }
    }

    private fun onForgotPasswordClicked() {}


    private val _stateFlow = MutableStateFlow(
        LoginPageState(
            onDropError = ::onDropError,
            onLoginClicked = ::onLoginClicked,
            onForgotPasswordClicked = ::onForgotPasswordClicked,
        ),
    )

    val stateFlow = _stateFlow.asStateFlow()
    private val state: LoginPageState
        get() = _stateFlow.value
}

data class LoginPageState(
    val passwordState: MutableState<String> = mutableStateOf("philips2010"),
    val authError: Boolean = false,
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val onDropError: () -> Unit,
    val onLoginClicked: () -> Unit,
    val onForgotPasswordClicked: () -> Unit,
)

class StateAble<T>(value: T, onStateChanged: (value: T) -> Unit = {}) {
    private val _state = MutableStateFlow(value)
    val state = _state.asStateFlow()
    val onChanged: (newValue: T) -> Unit = {newValue->
        onStateChanged(newValue)
        _state.update { newValue }
    }
}
