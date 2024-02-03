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

    private var name: String = ""
    private var amount: String = ""
    private var date: String = ""
    private var category: CategoryUiData = CategoryUiData("")
    private var photoUri: Uri? = null
    private var details: List<DetailUiData> = emptyList()
    private var isPlanned: Boolean = false
    private var isHidden: Boolean = false
    private var isManualTotal: Boolean = false
    private var currency: Currency = Currency.getInstance(Locale.getDefault())

    var onEditSpending: (String) -> Unit = {}
    private fun onEditClicked() {
        onEditSpending(spendingId)
    }

    private fun markPurchased() {
        viewModelScope.launch(Dispatchers.IO) {
            setPurchasedSpendingUseCase(spendingId)
            isPlanned = false
            updateUI()
        }
    }

    private fun createDuplicate() {
        viewModelScope.launch {
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
                    isManualTotal = isHidden,
                    isDuplicate = true,
                ),
            )
            saveDetailsUseCase(
                spendingId = duplicateSpendingId,
                details = details,
            )

            onEditSpending(duplicateSpendingId)
        }
    }

    private val state: SpendingShowState
        get() = SpendingShowState(
            id = spendingId,
            name = name,
            amount = amount,
            currency = currency,
            date = date,
            category = category,
            photoUri = photoUri,
            details = details,
            isPlanned = isPlanned,
            isHidden = isHidden,
            onMarkPurchasedClicked = ::markPurchased,
            onDuplicateClicked = ::createDuplicate,
            onEditClicked = ::onEditClicked,
        )
    private val _spendingsStateFlow = MutableStateFlow(state)
    val spendingsStateFlow = _spendingsStateFlow.asStateFlow().apply {
        viewModelScope.launch {
            if (spendingId.isNotBlank()) {
                val spending = getSpendingUseCase(spendingId)
                name = spending.name
                amount = spending.amount
                date = spending.date
                category = getCategoryByIdUseCase(spending.categoryId)
                photoUri = spending.photoUri
                details = getSpendingDetailsUseCase(spendingId)
                isPlanned = spending.isPlanned
                isHidden = spending.isHidden
                isManualTotal = spending.isManualTotal
                currency = getChosenCurrencyUseCase()
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
    val currency: Currency,
    val date: String,
    val category: CategoryUiData,
    val photoUri: Uri?,
    val details: List<DetailUiData>,
    val isHidden: Boolean,
    val isPlanned: Boolean,
    val onEditClicked: () -> Unit,
    val onDuplicateClicked: () -> Unit,
    val onMarkPurchasedClicked: () -> Unit,
)
