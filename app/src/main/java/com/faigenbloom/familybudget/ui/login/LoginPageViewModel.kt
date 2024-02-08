package com.faigenbloom.familybudget.ui.login

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

    private fun onLoginClicked() {
        viewModelScope.launch {
            if (loginUserUseCase(state.loginText, state.passwordText)) {
                onLoggedIn()
            } else {
                _stateFlow.update {
                    state.copy(
                        authError = true,
                    )
                }
            }
        }
    }

    private fun onLoginChanged(login: String) {
        _stateFlow.update {
            state.copy(
                loginText = login,
                authError = false,
            )
        }
    }

    private fun onPasswordChanged(password: String) {
        _stateFlow.update {
            state.copy(
                passwordText = password,
                authError = false,
            )
        }
    }

    private fun onForgotPasswordClicked() {}


    private val _stateFlow = MutableStateFlow(
        LoginPageState(
            onLoginClicked = ::onLoginClicked,
            onLoginChanged = ::onLoginChanged,
            onPasswordChanged = ::onPasswordChanged,
            onForgotPasswordClicked = ::onForgotPasswordClicked,
        ),
    )

    val stateFlow = _stateFlow.asStateFlow()
    private val state: LoginPageState
        get() = _stateFlow.value
}

data class LoginPageState(
    val loginText: String = "baskinaerobins@gmail.com",
    val passwordText: String = "philips2010",
    val authError: Boolean = false,
    val onLoginClicked: () -> Unit,
    var onLoginChanged: (String) -> Unit = {},
    var onPasswordChanged: (String) -> Unit = {},
    val onForgotPasswordClicked: () -> Unit,
)
