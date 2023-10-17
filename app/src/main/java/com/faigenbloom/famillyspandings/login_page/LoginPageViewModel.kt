package com.faigenbloom.famillyspandings.login_page

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginPageViewModel : ViewModel() {
    var loginText: String = ""
    var passwordText: String = ""
    var onLoginClicked: () -> Unit = {
        onLoggedIn()
    }
    var onLoggedIn: () -> Unit = {}
    val onLoginChanged: (String) -> Unit = {
        loginText = it
    }
    val onPasswordChanged: (String) -> Unit = {
        passwordText = it
    }

    val onForgotPasswordClicked: () -> Unit = {}

    val loginState: LoginPageState
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