package com.faigenbloom.famillyspandings.login_page

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginPageViewModel : ViewModel() {
    private var loginText: String = ""
    private var passwordText: String = ""
    private var onLoginClicked: () -> Unit = {
        onLoggedIn()
    }
    var onLoggedIn: () -> Unit = {}

    private val onLoginChanged: (String) -> Unit = {
        loginText = it
        _loginStateFlow.update { loginState }
    }
    private val onPasswordChanged: (String) -> Unit = {
        passwordText = it
        _loginStateFlow.update { loginState }
    }

    private val onForgotPasswordClicked: () -> Unit = {}

    private val loginState: LoginPageState
        get() = LoginPageState(
            loginText = loginText,
            passwordText = passwordText,
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
    val onLoginClicked: () -> Unit,
    var onLoginChanged: (String) -> Unit = {},
    var onPasswordChanged: (String) -> Unit = {},
    val onForgotPasswordClicked: () -> Unit
)