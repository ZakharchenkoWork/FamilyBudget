package com.faigenbloom.famillyspandings.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginPageViewModel(private val repository: LoginRepository) : ViewModel() {
    private var loginText: String = ""
    private var passwordText: String = ""
    private var authError: Boolean = false
    private var onLoginClicked: () -> Unit = {
        viewModelScope.launch {
            if (repository.login(loginText, passwordText)) {
                onLoggedIn()
            } else {
                authError = true
                _loginStateFlow.update { loginState }
            }
        }
    }
    var onLoggedIn: () -> Unit = {}

    private val onLoginChanged: (String) -> Unit = {
        loginText = it
        authError = false
        _loginStateFlow.update { loginState }
    }
    private val onPasswordChanged: (String) -> Unit = {
        passwordText = it
        authError = false
        _loginStateFlow.update { loginState }
    }

    private val onForgotPasswordClicked: () -> Unit = {}

    private val loginState: LoginPageState
        get() = LoginPageState(
            loginText = loginText,
            passwordText = passwordText,
            authError = authError,
            onLoginClicked = onLoginClicked,
            onLoginChanged = onLoginChanged,
            onPasswordChanged = onPasswordChanged,
            onForgotPasswordClicked = onForgotPasswordClicked,
        )

    private val _loginStateFlow = MutableStateFlow(loginState)
    val loginStateFlow = _loginStateFlow.asStateFlow()
}

data class LoginPageState(
    val loginText: String,
    val passwordText: String,
    val authError: Boolean = false,
    val onLoginClicked: () -> Unit,
    var onLoginChanged: (String) -> Unit = {},
    var onPasswordChanged: (String) -> Unit = {},
    val onForgotPasswordClicked: () -> Unit,
)
