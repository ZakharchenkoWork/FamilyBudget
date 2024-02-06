package com.faigenbloom.familybudget.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.domain.auth.RegisterUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterPageViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
) : ViewModel() {

    var onLoggedIn: () -> Unit = {}

    private fun isRegistrationEnabled(): Boolean {
        val isRegistrationEnabled =
            state.familyNameText.isNotBlank() && state.nameText.isNotBlank() &&
                    state.emailText.isNotBlank() && state.passwordText.isNotBlank() &&
                    !state.familyNameError && !state.nameError && !state.emailError && !state.passwordError
        _stateFlow.update { state.copy(isRegistrationEnabled = isRegistrationEnabled) }
        return isRegistrationEnabled
    }

    private fun onLoginClicked() {
        if (isRegistrationEnabled()) {
            viewModelScope.launch {
                if (registerUserUseCase(
                        state.nameText,
                        state.familyNameText,
                        state.emailText,
                        state.passwordText,
                    )
                ) {
                    onLoggedIn()
                } else {
                    _stateFlow.update { state.copy(registerError = true) }
                }
            }
        }
    }

    private fun onEmailChanged(email: String) {
        _stateFlow.update {
            state.copy(
                emailText = email,
                emailError = !isEmailValid(email),
            )
        }
        isRegistrationEnabled()
    }

    private fun onPasswordChanged(password: String) {
        _stateFlow.update {
            state.copy(
                passwordText = password,
                passwordError = !isValidPasswordFormat(password = password),
                isRegistrationEnabled = isRegistrationEnabled(),
            )
        }
        isRegistrationEnabled()
    }

    private fun onFamilyNameChanged(familyName: String) {
        _stateFlow.update {
            state.copy(
                familyNameText = familyName,
                familyNameError = familyName.isBlank(),
            )
        }
        isRegistrationEnabled()
    }

    private fun onNameChanged(name: String) {
        _stateFlow.update {
            state.copy(
                nameText = name,
                nameError = name.isBlank(),
            )
        }
        isRegistrationEnabled()
    }

    private fun onPrivacyPolicyClicked() {
        //TODO: ADD PRIVACY POLICY
    }

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

    private val state: RegisterPageState
        get() = _stateFlow.value

    private val _stateFlow = MutableStateFlow(
        RegisterPageState(
            onEmailChanged = ::onEmailChanged,
            onPasswordChanged = ::onPasswordChanged,
            onLoginClicked = ::onLoginClicked,
            onPrivacyPolicyClicked = ::onPrivacyPolicyClicked,
            onFamilyNameChanged = ::onFamilyNameChanged,
            onNameChanged = ::onNameChanged,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow()

}

data class RegisterPageState(
    val emailText: String = "baskinaerobins@gmail.com",
    val passwordText: String = "philips2010",
    val nameText: String = "Konstantyn",
    val familyNameText: String = "Zakharchenko",
    val registerError: Boolean = false,
    val nameError: Boolean = false,
    val familyNameError: Boolean = false,
    val passwordError: Boolean = false,
    val emailError: Boolean = false,
    val isRegistrationEnabled: Boolean = true, // TODO: CHANGE for release
    val onLoginClicked: () -> Unit,
    var onFamilyNameChanged: (String) -> Unit,
    var onEmailChanged: (String) -> Unit,
    val onNameChanged: (String) -> Unit,
    val onPasswordChanged: (String) -> Unit,
    val onPrivacyPolicyClicked: () -> Unit,
)
