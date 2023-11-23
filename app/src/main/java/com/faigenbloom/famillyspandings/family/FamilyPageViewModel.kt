package com.faigenbloom.famillyspandings.family

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class FamilyPageViewModel(
    repository: FamilyPageRepository,
) : ViewModel() {
    private var familyId = UUID.randomUUID().toString()
    private var name: String = ""
    private var familyList: List<String> = emptyList()
    private var isQRDialogOpened: Boolean = false

    private val onQRVisibilityChanged: (Boolean) -> Unit = {
        isQRDialogOpened = it
        _familyStateFlow.update { familyState }
    }
    private val familyState: FamilyState
        get() = FamilyState(
            familyId = familyId,
            name = name,
            familyList = familyList,
            onQRVisibilityChanged = onQRVisibilityChanged,
            isQRDialogOpened = isQRDialogOpened,
        )

    private val _familyStateFlow = MutableStateFlow(familyState)
    val familyStateFlow = _familyStateFlow.asStateFlow()
}

data class FamilyState(
    val familyId: String,
    val name: String,
    val familyList: List<String>,
    val isQRDialogOpened: Boolean,
    val onQRVisibilityChanged: (Boolean) -> Unit,
)
