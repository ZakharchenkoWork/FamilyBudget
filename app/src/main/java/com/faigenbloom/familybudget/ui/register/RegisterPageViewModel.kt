package com.faigenbloom.familybudget.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.domain.auth.RegisterUserUseCase
import com.faigenbloom.familybudget.domain.family.GetFamilyNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterPageViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getFamilyNameUseCase: GetFamilyNameUseCase,
) : ViewModel() {
    private var familyId: String = ""
    var onRegistered: () -> Unit = {}
    fun forFamily(id: String?) {
        id?.let {
            if (id.isNotBlank()) {
                familyId = id
                viewModelScope.launch {
                    val familyName = getFamilyNameUseCase(id)
                    _stateFlow.update {
                        it.copy(
                            isForFamily = id.isNotBlank(),
                            familyNameText = familyName,
                            surNameText = familyName,
                        )
                    }
                }
            }
        }
    }

    private fun isRegistrationEnabled(): Boolean {
        val isRegistrationEnabled =
            state.familyNameText.isNotBlank() && state.nameText.isNotBlank() &&
                    state.emailText.isNotBlank() && state.passwordText.isNotBlank() &&
                    !state.familyNameError && !state.nameError && !state.emailError && !state.passwordError
        _stateFlow.update { state.copy(isRegistrationEnabled = isRegistrationEnabled) }
        return isRegistrationEnabled
    }

    private fun onSurNameChanged(surname: String) {
        _stateFlow.update {
            state.copy(
                surNameText = surname,
            )
        }
    }

    private fun onSameFamilyNameSwitched(isEnabled: Boolean) {
        _stateFlow.update {
            state.copy(
                isSameFamilyName = isEnabled,
            )
        }
    }

    private fun onRegisterClicked() {
        if (isRegistrationEnabled()) {
            viewModelScope.launch {
                if (registerUserUseCase(
                        familyId = familyId,
                        name = state.nameText,
                        familyName = state.familyNameText,
                        surname = state.surNameText,
                        email = state.emailText,
                        password = state.passwordText,
                    )
                ) {
                    onRegistered()
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
                surNameText = familyName,
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
            onFamilyNameChanged = ::onFamilyNameChanged,
            onSurNameChanged = ::onSurNameChanged,
            onNameChanged = ::onNameChanged,
            onSameFamilyNameSwitched = ::onSameFamilyNameSwitched,
            onPrivacyPolicyClicked = ::onPrivacyPolicyClicked,
            onRegisterClicked = ::onRegisterClicked,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        _stateFlow.update {
            it.copy(
                emailText = "baskinaaaerobins@gmail.com",
                passwordText = "philips2010",
                nameText = "Nataly",
                isRegistrationEnabled = true, // TODO: CHANGE for release
            )
        }
    }
}

data class RegisterPageState(
    val isForFamily: Boolean = false,
    val emailText: String = "",
    val passwordText: String = "",
    val nameText: String = "",
    val familyNameText: String = "",
    val surNameText: String = "",
    val registerError: Boolean = false,
    val nameError: Boolean = false,
    val familyNameError: Boolean = false,
    val passwordError: Boolean = false,
    val emailError: Boolean = false,
    val isSameFamilyName: Boolean = true,
    val isRegistrationEnabled: Boolean = false,
    val onRegisterClicked: () -> Unit,
    var onFamilyNameChanged: (String) -> Unit,
    var onSurNameChanged: (String) -> Unit,
    val onSameFamilyNameSwitched: (Boolean) -> Unit,
    var onEmailChanged: (String) -> Unit,
    val onNameChanged: (String) -> Unit,
    val onPasswordChanged: (String) -> Unit,
    val onPrivacyPolicyClicked: () -> Unit,
)
