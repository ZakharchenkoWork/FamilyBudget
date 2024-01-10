package com.faigenbloom.famillyspandings.spandings.show

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.ID_ARG
import com.faigenbloom.famillyspandings.comon.toLocalDate
import com.faigenbloom.famillyspandings.comon.toReadableDate
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail
import kotlinx.coroutines.Dispatchers
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

    private var name: String = ""
    private var amount: Long = 0
    private var date: String = ""
    private var category: CategoryData = CategoryData("")
    private var photoUri: String? = null
    private var details: List<SpendingDetail> = emptyList()
    private var isPlanned: Boolean = false

    private var onMarkPurchasedClicked: () -> Unit = {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markSpendingPurchased(spendingId)
            isPlanned = false
            updateUI()
        }
    }
    private val state: SpendingShowState
        get() = SpendingShowState(
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
            isPlanned = isPlanned,
            onMarkPurchasedClicked = onMarkPurchasedClicked,
        )
    private val _spendingsStateFlow = MutableStateFlow(state)
    val spendingsStateFlow = _spendingsStateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            if (spendingId.isNotEmpty()) {
                val spending = repository.getSpending(spendingId)
                name = spending.name
                amount = spending.amount
                date = spending.date.toReadableDate()
                category = CategoryData.fromEntity(repository.getCategory(spending.categoryId))
                photoUri = spending.photoUri
                details = repository.getSpendingDetails(spendingId)
                    .map { SpendingDetail.fromEntity(it) }
                isPlanned = spending.isPlanned
                updateUI()
            }
        }
    }

    private fun updateUI() {
        _spendingsStateFlow.update { state }
    }
}

data class SpendingShowState(
    val id: String,
    val name: String,
    val amount: Long,
    val date: LocalDate,
    val category: CategoryData,
    val photoUri: String?,
    val details: List<SpendingDetail>,
    val isPlanned: Boolean,
    val onMarkPurchasedClicked: () -> Unit,
)
