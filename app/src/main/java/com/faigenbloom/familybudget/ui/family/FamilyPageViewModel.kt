package com.faigenbloom.familybudget.ui.family

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.domain.family.GetFamilyUseCase
import com.faigenbloom.familybudget.domain.family.SaveFamilyMembersUseCase
import com.faigenbloom.familybudget.domain.family.SaveFamilyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FamilyPageViewModel(
    private val getFamilyUseCase: GetFamilyUseCase,
    private val saveFamilyUseCase: SaveFamilyUseCase,
    private val saveFamilyMembersUseCase: SaveFamilyMembersUseCase,
) : ViewModel() {
    private val members: ArrayList<PersonUiData> = ArrayList()
    private fun onQRVisibilityChanged(isVisible: Boolean) {
        _stateFlow.update {
            state.copy(
                isQRDialogOpened = isVisible,
            )
        }
    }

    private fun onQrScanned(qrCode: String?) {
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
        viewModelScope.launch {
            saveFamilyUseCase.invoke(
                id = state.familyId,
                name = state.familyName,
            )
            saveFamilyMembersUseCase(
                members = members,
            )
        }
    }

    private val _stateFlow = MutableStateFlow(
        FamilyState(
            onQRVisibilityChanged = ::onQRVisibilityChanged,
            onQrScanned = ::onQrScanned,
            onSave = ::onSave,
            onFamilyNameChanged = ::onFamilyNameChanged,
            onDeleteFamilyMember = ::onDeleteFamilyMember,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val familyData = getFamilyUseCase()
            _stateFlow.update {
                it.copy(
                    familyId = familyData.id,
                    familyName = familyData.name,
                    familyList = familyData.members.map { member -> "${member.name} ${member.familyName}" },
                )
            }
        }
    }

    private val state: FamilyState
        get() = _stateFlow.value
}

data class FamilyState(
    val familyId: String = "",
    val familyName: String = "",
    val familyList: List<String> = emptyList(),
    val isQRDialogOpened: Boolean = false,
    val onQRVisibilityChanged: (Boolean) -> Unit,
    val onQrScanned: (String?) -> Unit,
    val onSave: () -> Unit,
    val onFamilyNameChanged: (String) -> Unit,
    val onDeleteFamilyMember: (index: Int) -> Unit,
)
