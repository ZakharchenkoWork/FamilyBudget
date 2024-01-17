package com.faigenbloom.famillyspandings.ui.spandings.show

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.comon.ID_ARG
import com.faigenbloom.famillyspandings.domain.SaveSpendingUseCase
import com.faigenbloom.famillyspandings.domain.SetPurchasedSpendingUseCase
import com.faigenbloom.famillyspandings.domain.categories.GetCategoryByIdUseCase
import com.faigenbloom.famillyspandings.domain.details.GetSpendingDetailsByIdUseCase
import com.faigenbloom.famillyspandings.domain.details.SaveDetailsUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetSpendingUseCase
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import com.faigenbloom.famillyspandings.ui.spandings.DetailUiData
import com.faigenbloom.famillyspandings.ui.spandings.SpendingUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpendingShowViewModel(
    savedStateHandle: SavedStateHandle,
    private val getSpendingUseCase: GetSpendingUseCase<SpendingUiData>,
    private val getSpendingDetailsUseCase: GetSpendingDetailsByIdUseCase<DetailUiData>,
    private val saveSpendingUseCase: SaveSpendingUseCase<SpendingUiData>,
    private val saveDetailsUseCase: SaveDetailsUseCase<DetailUiData>,
    private val setPurchasedSpendingUseCase: SetPurchasedSpendingUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase<CategoryUiData>,
) : ViewModel() {
    private var spendingId: String = savedStateHandle[ID_ARG] ?: ""

    private var name: String = ""
    private var amount: String = ""
    private var date: String = ""
    private var category: CategoryUiData = CategoryUiData("")
    private var photoUri: Uri? = null
    private var details: List<DetailUiData> = emptyList()
    private var isPlanned: Boolean = false
    private var isHidden: Boolean = false

    private fun markPurchased() {
        viewModelScope.launch(Dispatchers.IO) {
            setPurchasedSpendingUseCase(spendingId)
            isPlanned = false
            updateUI()
        }
    }

    private fun createDuplicate(
        onDuplicateCreated: (String) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val duplicateSpendingId = saveSpendingUseCase(
                spending = SpendingUiData(
                    id = "",
                    name = name,
                    amount = amount,
                    date = date,
                    categoryId = category.id,
                    photoUri = photoUri,
                    isPlanned = isPlanned,
                    isHidden = isHidden,
                    isDuplicate = true,
                ),
            )
            saveDetailsUseCase(
                spendingId = duplicateSpendingId,
                details = details,
            )
            viewModelScope.launch(Dispatchers.Main) {
                onDuplicateCreated(duplicateSpendingId)
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
                val spending = getSpendingUseCase(spendingId)
                name = spending.name
                amount = spending.amount
                date = spending.date
                category = getCategoryByIdUseCase(spending.categoryId)
                photoUri = spending.photoUri
                details = getSpendingDetailsUseCase(spendingId)
                isPlanned = spending.isPlanned
                isHidden = spending.isHidden
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
    val category: CategoryUiData,
    val photoUri: Uri?,
    val details: List<DetailUiData>,
    val isPlanned: Boolean,
    val onDuplicateClicked: (onDuplicateCreated: (String) -> Unit) -> Unit,
    val onMarkPurchasedClicked: () -> Unit,
)
