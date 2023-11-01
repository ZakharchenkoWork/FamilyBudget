package com.faigenbloom.famillyspandings.spandings.show

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.SPENDING_ID_ARG
import com.faigenbloom.famillyspandings.comon.toLocalDate
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpendingShowViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: SpendingsRepository,
) : ViewModel() {
    private var spendingId: String = savedStateHandle[SPENDING_ID_ARG] ?: ""

    private val _spendingsStateFlow = MutableStateFlow(
        SpendingShowState(
            spending = SpendingEntity(
                id = "",
                name = "",
                amount = 0L,
                date = "01.01.1970".toLocalDate(),
                category = CategoryData(
                    id = "",
                    nameId = null,
                    iconId = null,
                ),
                photoUri = null,
                details = listOf(),

            ),
        ),
    )
    val spendingsStateFlow = _spendingsStateFlow.asStateFlow().apply {
        viewModelScope.launch {
            if (spendingId.isNotEmpty()) {
                val spending = repository.getSpending(spendingId)
                _spendingsStateFlow.update {
                    SpendingShowState(spending = spending)
                }
            }
        }
    }
}

data class SpendingShowState(
    val spending: SpendingEntity,
)
