package com.faigenbloom.famillyspandings.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterPageViewModel(private val repository: RegisterRepository) : ViewModel() {
    private var nameText: String = ""
    private var familyNameText: String = ""
    private var emailText: String = ""
    private var passwordText: String = ""
    private var familyNameError: Boolean = false
    private var registerError: Boolean = false
    private var nameError: Boolean = false
    private var passwordError: Boolean = false
    private var emailError: Boolean = false
    private val isRegistrationEnabled: Boolean
        get() = familyNameText.isNotBlank() && nameText.isNotBlank() &&
                emailText.isNotBlank() && passwordText.isNotBlank() &&
                !familyNameError && !nameError && !emailError && !passwordError

    private var onLoginClicked: () -> Unit = {
        if (isRegistrationEnabled) {
            viewModelScope.launch {
                if (repository.register(nameText, familyNameText, emailText, passwordText)) {
                    onLoggedIn()
                } else {
                    registerError = true
                    _loginStateFlow.update { registerState }
                }
            }
        }
    }

    var onLoggedIn: () -> Unit = {}

    private val onEmailChanged: (String) -> Unit = {
        emailText = it
        emailError = !isEmailValid(emailText)
        _loginStateFlow.update { registerState }
    }
    private val onPasswordChanged: (String) -> Unit = {
        passwordText = it
        passwordError = !isValidPasswordFormat(password = passwordText)
        _loginStateFlow.update { registerState }
    }
    private val onFamilyNameChanged: (String) -> Unit = {
        familyNameText = it
        familyNameError = familyNameText.isBlank()
        _loginStateFlow.update { registerState }
    }
    private val onNameChanged: (String) -> Unit = {
        nameText = it
        nameError = nameText.isBlank()
        _loginStateFlow.update { registerState }
    }

    private val onPrivacyPolicyClicked: () -> Unit = {}

    private val registerState: RegisterPageState
        get() = RegisterPageState(
            emailText = emailText,
            onEmailChanged = onEmailChanged,
            passwordText = passwordText,
            nameText = nameText,
            familyNameText = familyNameText,
            registerError = registerError,
            nameError = nameError,
            familyNameError = familyNameError,
            passwordError = passwordError,
            emailError = emailError,
            isRegistrationEnabled = isRegistrationEnabled,
            onPasswordChanged = onPasswordChanged,
            onLoginClicked = onLoginClicked,
            onPrivacyPolicyClicked = onPrivacyPolicyClicked,
            onFamilyNameChanged = onFamilyNameChanged,
            onNameChanged = onNameChanged,
        )

    private val _loginStateFlow = MutableStateFlow(registerState)
    val loginStateFlow = _loginStateFlow.asStateFlow()
    private fun isValidPasswordFormat(password: String): Boolean {
        val passwordREGEX = Pattern.compile(
            "^" +
                "(?=\\S+$)" + // no white spaces
                ".{8,}" + // at least 8 characters
                "$",
        )
        return passwordREGEX.matcher(password).matches()
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

data class RegisterPageState(
    val emailText: String,
    val passwordText: String,
    val nameText: String,
    val familyNameText: String,
    val registerError: Boolean,
    val nameError: Boolean,
    val familyNameError: Boolean,
    val passwordError: Boolean,
    val emailError: Boolean,
    val isRegistrationEnabled: Boolean,
    val onLoginClicked: () -> Unit,
    var onFamilyNameChanged: (String) -> Unit,
    var onEmailChanged: (String) -> Unit,
    val onNameChanged: (String) -> Unit,
    val onPasswordChanged: (String) -> Unit,
    val onPrivacyPolicyClicked: () -> Unit,
)
