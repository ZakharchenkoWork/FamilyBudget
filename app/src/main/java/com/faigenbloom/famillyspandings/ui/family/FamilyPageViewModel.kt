package com.faigenbloom.famillyspandings.ui.family

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.domain.family.GetFamilyUseCase
import com.faigenbloom.famillyspandings.domain.family.SaveFamilyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FamilyPageViewModel(
    private val getFamilyUseCase: GetFamilyUseCase,
    private val saveFamilyUseCase: SaveFamilyUseCase,
) : ViewModel() {
    private fun onQRVisibilityChanged(isVisible: Boolean) {
        _stateFlow.update {
            it.copy(
                isQRDialogOpened = isVisible,
            )
        }
    }

    private fun onQrScanned(qrCode: String?) {
    }

    private fun onFamilyNameChanged(name: String) {
        _stateFlow.update {
            it.copy(
                familyName = name,
            )
        }
    }

    private fun onDeleteFamilyMember(index: Int) {
        _stateFlow.update {
            it.copy(
                familyList = ArrayList(it.familyList).apply { removeAt(index) },
            )
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            saveFamilyUseCase(
                id = _stateFlow.value.familyId,
                name = _stateFlow.value.familyName,
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
    val stateFlow = _stateFlow.asStateFlow().apply {
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
