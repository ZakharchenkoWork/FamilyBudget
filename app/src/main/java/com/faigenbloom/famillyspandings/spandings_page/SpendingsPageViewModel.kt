package com.faigenbloom.famillyspandings.spandings_page

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SpendingsPageViewModel : ViewModel() {

    private var spendings: List<SpendingData> = emptyList()

    init {
        spendings = Mock.spendingsList
    }

    val spendingsState: SpendingsState
        get() = SpendingsState(spendings)

    private val _spendingsStateFlow = MutableStateFlow(spendingsState)
    val spendingsStateFlow =_spendingsStateFlow.asStateFlow()
}

data class SpendingsState(val spendings: List<SpendingData>)