package com.faigenbloom.famillyspandings.spandings.show

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.ID_ARG
import com.faigenbloom.famillyspandings.comon.toReadableDate
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpendingShowViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: SpendingsRepository,
) : ViewModel() {
    private var spendingId: String = savedStateHandle[ID_ARG] ?: ""

    private var name: String = ""
    private var amount: String = ""
    private var date: String = ""
    private var category: CategoryData = CategoryData("")
    private var photoUri: Uri? = null
    private var details: List<SpendingDetail> = emptyList()
    private var isPlanned: Boolean = false

    private fun markPurchased() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markSpendingPurchased(spendingId)
            isPlanned = false
            updateUI()
        }
    }

    private fun createDuplicate(
        onDuplicateCreated: (String) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = repository.duplicateSpending(
                name = name,
                amount = amount,
                date = date,
                category = category,
                photoUri = photoUri,
                isPlanned = isPlanned,
                details = details.map { it.mapToEntity() },
            )
            viewModelScope.launch(Dispatchers.Main) {
                onDuplicateCreated(id)
            }
        }
    }

    private val state: SpendingShowState
        get() = SpendingShowState(
            id = spendingId,
            name = name,
            amount = amount,
            date = date,
            category = category,
            photoUri = photoUri,
            details = details,
            isPlanned = isPlanned,
            onMarkPurchasedClicked = ::markPurchased,
            onDuplicateClicked = ::createDuplicate,
        )
    private val _spendingsStateFlow = MutableStateFlow(state)
    val spendingsStateFlow = _spendingsStateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            if (spendingId.isNotEmpty()) {
                val spending = repository.getSpending(spendingId)
                name = spending.name
                amount = spending.amount.toReadableMoney()
                date = spending.date.toReadableDate()
                category = CategoryData.fromEntity(repository.getCategory(spending.categoryId))
                photoUri = spending.photoUri?.toUri()
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
    val amount: String,
    val date: String,
    val category: CategoryData,
    val photoUri: Uri?,
    val details: List<SpendingDetail>,
    val isPlanned: Boolean,
    val onDuplicateClicked: (onDuplicateCreated: (String) -> Unit) -> Unit,
    val onMarkPurchasedClicked: () -> Unit,
)
