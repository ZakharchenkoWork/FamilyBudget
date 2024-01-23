package com.faigenbloom.famillyspandings.ui.spendings.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.common.toNormalizedMoney
import com.faigenbloom.famillyspandings.domain.GetChosenCurrencyUseCase
import com.faigenbloom.famillyspandings.domain.details.GetAllSpendingDetailsUseCase
import com.faigenbloom.famillyspandings.ui.spendings.DetailUiData
import com.faigenbloom.famillyspandings.ui.spendings.SpendingDetailListWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getAllSpendingDetailsUseCase: GetAllSpendingDetailsUseCase<DetailUiData>,
    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,
) : ViewModel() {
    private var spendingDetails: ArrayList<DetailUiData> =
        DetailsDialogArgs(savedStateHandle).getList()
    private var newSpendingId: String = ""
    private var spendingId: String = ""
    private var nameText: String = ""
    private var amountText: String = ""
    private var barcodeText: String = ""
    private var allDetails: List<DetailUiData> = emptyList()
    private var suggestions: List<DetailUiData> = emptyList()
    private var suggestionChosen: Int = -1
    private var detailChosen: Int = -1
    private var isBarcodeScannerVisible: Boolean = false
    private var currency: Currency = Currency.getInstance(Locale.getDefault())

    var onSave: (updateDetails: SpendingDetailListWrapper) -> Unit = { }
    private fun onDetailClicked(index: Int) {
        nameText = spendingDetails[index].name
        amountText = spendingDetails[index].amount
        barcodeText = spendingDetails[index].barcode
        detailChosen = index
        updateUI()
    }

    private fun onNameChanged(name: String) {
        nameText = name
        suggestions = if (name.isNotBlank()) {
            allDetails
                .filter { isFilterMatched(it.name, name) }
        } else {
            emptyList()
        }
        updateUI()
    }

    private fun onAmountChanged(amount: String) {
        amountText = amount.toNormalizedMoney()
        updateUI()
    }

    private fun addDetailToList() {
        spendingDetails = ArrayList(spendingDetails).apply {
            add(
                DetailUiData(
                    id = newSpendingId,
                    name = nameText,
                    amount = amountText,
                    barcode = barcodeText,
                ),
            )
        }
        nameText = ""
        amountText = ""
        barcodeText = ""
        suggestions = emptyList()
        updateUI()
    }

    private fun onBarCodeVisibilityChange() {
        isBarcodeScannerVisible = !isBarcodeScannerVisible
        updateUI()
    }

    private fun onDeleteDetail(index: Int) {
        spendingDetails = ArrayList(spendingDetails).apply {
            removeAt(index)
        }

        updateUI()
    }

    private fun onBarCodeFound(barCode: String) {
        isBarcodeScannerVisible = false
        suggestions = if (barCode.isNotBlank()) {
            allDetails
                .filter { isFilterMatched(it.barcode, barCode) }
        } else {
            emptyList()
        }
        barcodeText = barCode
        updateUI()
    }

    private fun isFilterMatched(itemName: String, name: String): Boolean {
        return itemName.lowercase().contains(name.lowercase())
    }

    private fun onSuggestionClicked(index: Int) {
        suggestionChosen = index
        val detail = suggestions[index]
        newSpendingId = detail.id
        nameText = detail.name
        amountText = detail.amount
        barcodeText = detail.barcode
        updateUI()
    }

    private fun onOkClicked() {
        onSave(SpendingDetailListWrapper(spendingDetails))
    }

    private val state: DetailDialogState
        get() = DetailDialogState(
            spendingId = spendingId,
            spendingDetails = spendingDetails,
            name = nameText,
            amount = amountText,
            barcodeText = barcodeText,
            suggestions = suggestions,
            detailChosen = detailChosen,
            suggestionChosen = suggestionChosen,
            isBarcodeScannerVisible = isBarcodeScannerVisible,
            currency = currency,
            onDetailClicked = ::onDetailClicked,
            addDetailToList = ::addDetailToList,
            onNameChanged = ::onNameChanged,
            onAmountChanged = ::onAmountChanged,
            onBarCodeFound = ::onBarCodeFound,
            onBarCodeVisibilityChange = ::onBarCodeVisibilityChange,
            onSuggestionClicked = ::onSuggestionClicked,
            onDeleteDetail = ::onDeleteDetail,
            onOkClicked = ::onOkClicked,
        )

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            allDetails = getAllSpendingDetailsUseCase()
            currency = getChosenCurrencyUseCase()
            updateUI()
        }
    }

    private fun updateUI() {
        _stateFlow.update { state }
    }
}

data class DetailDialogState(
    val spendingId: String,
    val name: String,
    val amount: String,
    val barcodeText: String,
    val spendingDetails: List<DetailUiData>,
    val detailChosen: Int,
    val suggestions: List<DetailUiData>,
    val suggestionChosen: Int,
    val isBarcodeScannerVisible: Boolean,
    val currency: Currency,
    val onNameChanged: (String) -> Unit,
    val onAmountChanged: (String) -> Unit,
    val onBarCodeFound: (barCode: String) -> Unit,
    val onSuggestionClicked: (Int) -> Unit,
    val onDetailClicked: (Int) -> Unit,
    val onBarCodeVisibilityChange: () -> Unit,
    val addDetailToList: () -> Unit,
    val onDeleteDetail: (index: Int) -> Unit,
    val onOkClicked: () -> Unit,
)
