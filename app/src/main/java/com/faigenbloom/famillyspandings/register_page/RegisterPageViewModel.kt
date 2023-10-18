package com.faigenbloom.famillyspandings.register_page

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterPageViewModel : ViewModel() {
    private var nameText: String = ""
    private var familyNameText: String = ""
    private var loginText: String = ""
    private var passwordText: String = ""
    private var passwordRepeatText: String = ""


    private var onLoginClicked: () -> Unit = {
        onLoggedIn()
    }
    var onLoggedIn: () -> Unit = {}

    private val onLoginChanged: (String) -> Unit = {
        loginText = it
        _loginStateFlow.update { registerState }
    }
    private val onPasswordChanged: (String) -> Unit = {
        passwordText = it
        _loginStateFlow.update { registerState }
    }
    private val onFamilyNameChanged: (String) -> Unit = {
        familyNameText = it
        _loginStateFlow.update { registerState }
    }
    private val onNameChanged: (String) -> Unit = {
        nameText = it
        _loginStateFlow.update { registerState }
    }
    private val onPasswordRepeatChanged: (String) -> Unit = {
        passwordRepeatText = it
        _loginStateFlow.update { registerState }
    }


    private val onPrivacyPolicyClicked: () -> Unit = {}

    private val registerState: RegisterPageState
        get() = RegisterPageState(
            loginText = loginText,
            onLoginChanged = onLoginChanged,
            passwordText = passwordText,
            nameText = nameText,
            familyNameText = familyNameText,
            passwordRepeatText = passwordRepeatText,
            onPasswordChanged = onPasswordChanged,
            onLoginClicked = onLoginClicked,
            onPrivacyPolicyClicked = onPrivacyPolicyClicked,
            onFamilyNameChanged = onFamilyNameChanged,
            onPasswordRepeatChanged = onPasswordRepeatChanged,
            onNameChanged = onNameChanged
        )

    private val _loginStateFlow = MutableStateFlow(registerState)
    val loginStateFlow = _loginStateFlow.asStateFlow()
}

data class RegisterPageState(
    val loginText: String,
    val passwordText: String,
    val nameText: String,
    val familyNameText: String,
    val passwordRepeatText: String,
    val onLoginClicked: () -> Unit,
    var onFamilyNameChanged: (String) -> Unit,
    var onLoginChanged: (String) -> Unit,
    val onNameChanged: (String) -> Unit,
    val onPasswordChanged: (String) -> Unit,
    val onPasswordRepeatChanged: (String) -> Unit,
    val onPrivacyPolicyClicked: () -> Unit
)