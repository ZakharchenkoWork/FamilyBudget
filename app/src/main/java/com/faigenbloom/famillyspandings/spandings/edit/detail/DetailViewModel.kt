package com.faigenbloom.famillyspandings.spandings.edit.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.comon.DetailsDialogArgs
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetailListWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    val repository: DetailsRepository,
) : ViewModel() {
    private var spendingDetails: ArrayList<SpendingDetail> =
        DetailsDialogArgs(savedStateHandle).getList()
    private var newSpendingId: String = ""
    private var spendingId: String = ""
    private var nameText: String = ""
    private var amountText: String = ""
    private var barcodeText: String = ""
    private var allDetails: List<SpendingDetailEntity> = emptyList()
    private var suggestions: List<SpendingDetail> = emptyList()
    private var suggestionChosen: Int = -1
    private var detailChosen: Int = -1
    private var isBarcodeScannerVisible: Boolean = false

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
                .map { SpendingDetail.fromEntity(it) }
        } else {
            emptyList()
        }
        updateUI()
    }

    private fun onAmountChanged(amount: String) {
        amountText = amount
        updateUI()
    }

    private fun addDetailToList() {
        spendingDetails = ArrayList(spendingDetails).apply {
            add(
                SpendingDetail(
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
                .map { SpendingDetail.fromEntity(it) }
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
            allDetails = repository.getAllDetails()
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
    val spendingDetails: List<SpendingDetail>,
    val detailChosen: Int,
    val suggestions: List<SpendingDetail>,
    val suggestionChosen: Int,
    val isBarcodeScannerVisible: Boolean,
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
