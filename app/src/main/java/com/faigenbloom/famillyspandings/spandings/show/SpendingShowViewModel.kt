package com.faigenbloom.famillyspandings.spandings.show

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.ID_ARG
import com.faigenbloom.famillyspandings.comon.toLocalDate
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class SpendingShowViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: SpendingsRepository,
) : ViewModel() {
    private var spendingId: String = savedStateHandle[ID_ARG] ?: ""

    private val _spendingsStateFlow = MutableStateFlow(
        SpendingShowState(
            spending = SpendingForUI(
                id = spendingId,
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
                val category = repository.getCategory(spending.categoryId)
                val spendingDetails = repository.getSpendingDetails(spendingId)

                _spendingsStateFlow.update {
                    SpendingShowState(
                        spending = SpendingForUI(
                            id = spending.id,
                            name = spending.name,
                            amount = spending.amount,
                            date = spending.date.toLocalDate(),
                            category = CategoryData.fromEntity(category),
                            photoUri = spending.photoUri,
                            details = spendingDetails.map { SpendingDetail.fromEntity(it) },
                        ),
                    )
                }
            }
        }
    }
}

data class SpendingForUI(
    val id: String,
    val name: String,
    val amount: Long,
    val date: LocalDate,
    val category: CategoryData,
    val photoUri: String?,
    val details: List<SpendingDetail>,
)

data class SpendingShowState(
    val spending: SpendingForUI,
)
