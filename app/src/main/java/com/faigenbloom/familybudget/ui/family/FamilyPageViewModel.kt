package com.faigenbloom.familybudget.ui.family

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.common.OPTIONAL_ID_ARG
import com.faigenbloom.familybudget.common.ui.AnimationState
import com.faigenbloom.familybudget.domain.family.GetFamilyLinkUseCase
import com.faigenbloom.familybudget.domain.family.GetFamilyNameUseCase
import com.faigenbloom.familybudget.domain.family.GetFamilyUseCase
import com.faigenbloom.familybudget.domain.family.LeaveFamilyUseCase
import com.faigenbloom.familybudget.domain.family.MigrateFamilyUseCase
import com.faigenbloom.familybudget.domain.family.Result
import com.faigenbloom.familybudget.domain.family.SaveFamilyMembersUseCase
import com.faigenbloom.familybudget.domain.family.SaveFamilyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FamilyPageViewModel(
    savedStateHandle: SavedStateHandle,
    private val getFamilyUseCase: GetFamilyUseCase,
    private val saveFamilyUseCase: SaveFamilyUseCase,
    private val saveFamilyMembersUseCase: SaveFamilyMembersUseCase,
    private val getFamilyNameUseCase: GetFamilyNameUseCase,
    private val migrateFamilyUseCase: MigrateFamilyUseCase,
    private val leaveFamilyUseCase: LeaveFamilyUseCase,
    private val getFamilyLinkUseCase: GetFamilyLinkUseCase,
) : ViewModel() {
    private val members: ArrayList<PersonUiData> = ArrayList()
    private var newFamilyId: String = ""
    var onLinkShareRequest: (String) -> Unit = {}
    fun onNewFamilyIdReceived(familyId: String) {
        showLoading()
        newFamilyId = familyId
        viewModelScope.launch {
            _stateFlow.update { state ->
                val familyName = getFamilyNameUseCase(newFamilyId)
                if (state.familyId == newFamilyId) {
                    state.copy(
                        isLoading = false,
                        migrationErrorDialogState = state.migrationErrorDialogState.copy(
                            isShown = true,
                            isNotFound = false,
                        ),
                    )
                } else if (familyName.isNotBlank().not()) {
                    state.copy(
                        isLoading = false,
                        migrationErrorDialogState = state.migrationErrorDialogState.copy(
                            isShown = true,
                            isNotFound = true,
                        ),
                    )
                } else {
                    state.copy(
                        isLoading = false,
                        migrationDialogState = state.migrationDialogState.copy(
                            familyName = familyName,
                            isMigrationDialogOpened = true,
                        ),
                    )
                }
            }
        }
    }

    private fun onQRVisibilityChanged(isVisible: Boolean) {
        _stateFlow.update {
            state.copy(
                isQRDialogOpened = isVisible,
            )
        }
    }

    private fun onFamilyNameChanged(name: String) {
        _stateFlow.update {
            state.copy(
                familyName = name,
            )
        }
    }

    private fun onDeleteFamilyMember(index: Int) {
        members.removeAt(index)
        _stateFlow.update {
            state.copy(
                familyList = ArrayList(it.familyList).apply { removeAt(index) },
            )
        }
    }

    private fun onSave() {
        showLoading()
        viewModelScope.launch {
            saveFamilyUseCase.invoke(
                id = state.familyId,
                name = state.familyName,
            )
            saveFamilyMembersUseCase(
                members = members,
            )
            hideLoading()
        }
    }

    private fun onLeaveFamily() {
        _stateFlow.update { state ->
            state.copy(
                exitFamilyDialogState = state.exitFamilyDialogState.copy(
                    isShown = true,
                ),
            )
        }
    }

    private fun onMigration() {
        showLoading()
        viewModelScope.launch {
            onMigrationDialogVisibilityChanged(false)
            _stateFlow.update { state ->
                when (migrateFamilyUseCase(
                    newFamilyId,
                    state.migrationDialogState.isHideUserSpending,
                )) {
                    Result.NoSuchFamily -> state.copy(
                        successState = state.successState.copy(isShown = false),
                        isLoading = false,
                        migrationErrorDialogState = state.migrationErrorDialogState.copy(
                            isShown = true,
                            isNotFound = true,
                        ),
                    )

                    Result.SameFamily -> state.copy(
                        successState = state.successState.copy(isShown = false),
                        isLoading = false,
                        migrationErrorDialogState = state.migrationErrorDialogState.copy(
                            isShown = true,
                            isNotFound = false,
                        ),
                    )

                    Result.Success -> {
                        reload()
                        state.copy(
                            successState = state.successState.copy(isShown = true),
                            isLoading = false,
                            migrationErrorDialogState = state.migrationErrorDialogState.copy(
                                isShown = false,
                                isNotFound = false,
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun onMigrationDialogVisibilityChanged(isVisible: Boolean) {
        _stateFlow.update { state ->
            state.copy(
                migrationDialogState = state.migrationDialogState.copy(
                    isMigrationDialogOpened = isVisible,
                ),
            )
        }
    }

    private fun onHideUserSpendingChanged(isHidden: Boolean) {
        _stateFlow.update { state ->
            state.copy(
                migrationDialogState = state.migrationDialogState.copy(
                    isHideUserSpending = isHidden,
                ),
            )
        }
    }

    private fun hideMigrationSuccess() {
        _stateFlow.update { state ->
            state.copy(
                successState = state.successState.copy(
                    isShown = false,
                ),
            )
        }
    }

    private fun onHideWarningDialog() {
        _stateFlow.update { state ->
            state.copy(
                exitFamilyDialogState = state.exitFamilyDialogState.copy(
                    isShown = false,
                ),
            )
        }
    }

    private fun onShareLink() {
        viewModelScope.launch {
            onLinkShareRequest(getFamilyLinkUseCase(state.familyId))
        }
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

    private fun onDialogFamilyNameChanged(newFamilyName: String) {
        _stateFlow.update { state ->
            state.copy(
                exitFamilyDialogState = state.exitFamilyDialogState.copy(
                    familyName = newFamilyName,
                ),
            )
        }
    }

    private fun onExitFamily() {
        _stateFlow.update { state ->
            state.copy(
                isLoading = true,
                exitFamilyDialogState = state.exitFamilyDialogState
                    .copy(isShown = false),
            )
        }
        viewModelScope.launch {
            leaveFamilyUseCase(state.exitFamilyDialogState.familyName)
            reload()
            _stateFlow.update { state ->
                state.copy(
                    isLoading = false,
                    successState = state.successState
                        .copy(isShown = true),
                )
            }
        }
    }

    private fun showLoading() {
        _stateFlow.update { state ->
            state.copy(
                isLoading = true,
            )
        }
    }

    private fun hideLoading() {
        _stateFlow.update { state ->
            state.copy(
                isLoading = false,
            )
        }
    }

    private val _stateFlow = MutableStateFlow(
        FamilyState(
            migrationDialogState = MigrationDialogState(
                isMigrationDialogOpened = false,
                onMigrationDialogVisibilityChanged = ::onMigrationDialogVisibilityChanged,
                onHideUserSpendingChanged = ::onHideUserSpendingChanged,
                onOkClicked = ::onMigration,
            ),
            successState = AnimationState(
                isShown = false,
                onFinish = ::hideMigrationSuccess,
            ),
            exitFamilyDialogState = ExitFamilyDialogState(
                isShown = false,
                onDialogFamilyNameChanged = ::onDialogFamilyNameChanged,
                onHideDialog = ::onHideWarningDialog,
                onOkClicked = ::onExitFamily,
            ),
            onQRVisibilityChanged = ::onQRVisibilityChanged,
            onSave = ::onSave,
            onFamilyNameChanged = ::onFamilyNameChanged,
            onDeleteFamilyMember = ::onDeleteFamilyMember,
            onLeaveFamily = ::onLeaveFamily,
            migrationErrorDialogState = MigrationErrorDialogState(
                isShown = false,
                isNotFound = false,
                onHideDialog = ::onHideMigrationErrorDialog,
            ),
            familyId = "",
            familyName = "",
            familyList = listOf(),
            isLoading = false,
            onShareLink = ::onShareLink,
            isQRDialogOpened = false,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow()
    private val state: FamilyState
        get() = _stateFlow.value

    init {
        val value: String? = savedStateHandle[OPTIONAL_ID_ARG]
        value?.let {
            if (it.isNotBlank()) {
                onNewFamilyIdReceived(it)
            }
        }
        reload()
    }

    private fun reload() {
        viewModelScope.launch {
            val familyData = getFamilyUseCase()
            _stateFlow.update {
                it.copy(
                    isLoading = false,
                    familyId = familyData.id,
                    familyName = familyData.name,
                    canLeaveFamily = familyData.members.size > 1,
                    familyList = familyData.members.map { member -> "${member.name} ${member.familyName}" },
                    exitFamilyDialogState = it.exitFamilyDialogState.copy(familyName = familyData.name),
                )
            }
        }
    }
}

data class FamilyState(
    val migrationDialogState: MigrationDialogState,
    val successState: AnimationState,
    val migrationErrorDialogState: MigrationErrorDialogState,
    val exitFamilyDialogState: ExitFamilyDialogState,
    val familyId: String = "",
    val familyName: String = "",
    val familyList: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val canLeaveFamily: Boolean = true,
    val isQRDialogOpened: Boolean = false,
    val onQRVisibilityChanged: (Boolean) -> Unit,
    val onShareLink: () -> Unit = {},
    val onLeaveFamily: () -> Unit = {},
    val onSave: () -> Unit = {},
    val onFamilyNameChanged: (String) -> Unit,
    val onDeleteFamilyMember: (index: Int) -> Unit,
)

data class MigrationDialogState(
    val familyName: String = "",
    val isMigrationDialogOpened: Boolean = false,
    val isHideUserSpending: Boolean = false,
    val onHideUserSpendingChanged: (Boolean) -> Unit,
    val onMigrationDialogVisibilityChanged: (Boolean) -> Unit,
    val onOkClicked: () -> Unit = {},
)

data class MigrationErrorDialogState(
    val isShown: Boolean = false,
    val isNotFound: Boolean = true,
    val onHideDialog: () -> Unit = {},
)

data class ExitFamilyDialogState(
    val familyName: String = "",
    val onDialogFamilyNameChanged: (String) -> Unit,
    val isShown: Boolean = false,
    val onHideDialog: () -> Unit = {},
    val onOkClicked: () -> Unit = {},
)
