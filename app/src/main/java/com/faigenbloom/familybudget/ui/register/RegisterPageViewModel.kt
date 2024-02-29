package com.faigenbloom.familybudget.ui.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.common.OPTIONAL_ID_ARG
import com.faigenbloom.familybudget.domain.auth.RegisterUserUseCase
import com.faigenbloom.familybudget.domain.family.GetFamilyNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterPageViewModel(
    savedStateHandle: SavedStateHandle,
    private val registerUserUseCase: RegisterUserUseCase,
    private val getFamilyNameUseCase: GetFamilyNameUseCase,
) : ViewModel() {
    private var familyId: String = ""
    var onRegistered: () -> Unit = {}
    fun forFamily(id: String?) {

        id?.let {
            if (id.isNotBlank()) {
                familyId = id
                state.isLoading.value = true
                viewModelScope.launch {
                    val familyName = getFamilyNameUseCase(id)
                    if (familyName.isNotBlank()) {
                        _stateFlow.update {
                            it.copy(
                                isForFamily = id.isNotBlank(),
                                familyNameText = familyName,
                                surNameText = familyName,
                            )
                        }
                    } else {
                        _stateFlow.update {
                            it.copy(
                                migrationErrorDialogState = state.migrationErrorDialogState.copy(
                                    isShown = true,
                                ),
                            )
                        }
                    }
                    state.isLoading.value = false
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
            state.isLoading.value = true
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
                state.isLoading.value = false
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

    private fun onHideMigrationErrorDialog() {
        _stateFlow.update { state ->
            state.copy(
                migrationErrorDialogState = state.migrationErrorDialogState.copy(
                    isShown = false,
                ),
            )
        }
    }

    private val state: RegisterPageState
        get() = _stateFlow.value
    private val _stateFlow = MutableStateFlow(
        RegisterPageState(
            migrationErrorDialogState = MigrationErrorDialogState(
                isShown = false,
                onHideDialog = ::onHideMigrationErrorDialog,
            ),
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
        val value: String? = savedStateHandle[OPTIONAL_ID_ARG]
        value?.let {
            if (it.isNotBlank()) {
                forFamily(it)
            }
        }
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
    val migrationErrorDialogState: MigrationErrorDialogState = MigrationErrorDialogState(
        isShown = false,
        onHideDialog = {},
    ),
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
    val isLoading: MutableState<Boolean> = mutableStateOf(true),
    val onRegisterClicked: () -> Unit,
    var onFamilyNameChanged: (String) -> Unit,
    var onSurNameChanged: (String) -> Unit,
    val onSameFamilyNameSwitched: (Boolean) -> Unit,
    var onEmailChanged: (String) -> Unit,
    val onNameChanged: (String) -> Unit,
    val onPasswordChanged: (String) -> Unit,
    val onPrivacyPolicyClicked: () -> Unit,
)

data class MigrationErrorDialogState(
    val isShown: Boolean = false,
    val onHideDialog: () -> Unit = {},
)
