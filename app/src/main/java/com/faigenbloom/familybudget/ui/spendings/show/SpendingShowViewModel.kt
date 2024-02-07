package com.faigenbloom.familybudget.ui.spendings.show

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.common.ID_ARG
import com.faigenbloom.familybudget.domain.GetChosenCurrencyUseCase
import com.faigenbloom.familybudget.domain.SetPurchasedSpendingUseCase
import com.faigenbloom.familybudget.domain.categories.GetCategoryByIdUseCase
import com.faigenbloom.familybudget.domain.details.GetSpendingDetailsByIdUseCase
import com.faigenbloom.familybudget.domain.details.SaveDetailsUseCase
import com.faigenbloom.familybudget.domain.spendings.GetSpendingUseCase
import com.faigenbloom.familybudget.domain.spendings.SaveSpendingUseCase
import com.faigenbloom.familybudget.ui.categories.CategoryUiData
import com.faigenbloom.familybudget.ui.spendings.DetailUiData
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

class SpendingShowViewModel(
    savedStateHandle: SavedStateHandle,
    private val getSpendingUseCase: GetSpendingUseCase<SpendingUiData>,
    private val getSpendingDetailsUseCase: GetSpendingDetailsByIdUseCase<DetailUiData>,
    private val saveSpendingUseCase: SaveSpendingUseCase<SpendingUiData>,
    private val saveDetailsUseCase: SaveDetailsUseCase<DetailUiData>,
    private val setPurchasedSpendingUseCase: SetPurchasedSpendingUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase<CategoryUiData>,
    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,
) : ViewModel() {
    private var spendingId: String = savedStateHandle[ID_ARG] ?: ""
    private var isManualTotal: Boolean = false

    var onEditSpending: (String) -> Unit = {}
    private fun onEditClicked() {
        onEditSpending(spendingId)
    }

    private fun markPurchased() {
        viewModelScope.launch(Dispatchers.IO) {
            setPurchasedSpendingUseCase(spendingId)
            _stateFlow.update {
                state.copy(
                    isPlanned = false,
                )
            }
        }
    }

    private fun createDuplicate() {
        viewModelScope.launch {
            val duplicateSpendingId = saveSpendingUseCase(
                spending = SpendingUiData(
                    id = "",
                    name = state.name,
                    amount = state.amount,
                    date = state.date,
                    categoryId = state.category.id,
                    photoUri = state.photoUri,
                    isPlanned = state.isPlanned,
                    isHidden = state.isHidden,
                    isManualTotal = isManualTotal,
                    isDuplicate = true,
                    ownerId = "",
                ),
            )
            saveDetailsUseCase(
                spendingId = duplicateSpendingId,
                details = state.details,
                isDuplicate = true,
            )

            onEditSpending(duplicateSpendingId)
        }
    }

    private val state: SpendingShowState
        get() = _stateFlow.value

    private val _stateFlow = MutableStateFlow(
        SpendingShowState(
            onMarkPurchasedClicked = ::markPurchased,
            onDuplicateClicked = ::createDuplicate,
            onEditClicked = ::onEditClicked,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow().apply {
        viewModelScope.launch {
            if (spendingId.isNotBlank()) {
                val spending = getSpendingUseCase(spendingId)

                isManualTotal = spending.isManualTotal

                _stateFlow.update {
                    state.copy(
                        name = spending.name,
                        amount = spending.amount,
                        date = spending.date,
                        category = getCategoryByIdUseCase(spending.categoryId),
                        photoUri = spending.photoUri,
                        details = getSpendingDetailsUseCase(spendingId),
                        isPlanned = spending.isPlanned,
                        isHidden = spending.isHidden,
                        currency = getChosenCurrencyUseCase(),
                    )
                }
            }
        }
    }

}

data class SpendingShowState(
    val name: String = "",
    val amount: String = "",
    val currency: Currency = Currency.getInstance(Locale.getDefault()),
    val date: String = "",
    val category: CategoryUiData = CategoryUiData(""),
    val photoUri: Uri? = null,
    val details: List<DetailUiData> = emptyList(),
    val isHidden: Boolean = false,
    val isPlanned: Boolean = false,
    val onEditClicked: () -> Unit,
    val onDuplicateClicked: () -> Unit,
    val onMarkPurchasedClicked: () -> Unit,
)
