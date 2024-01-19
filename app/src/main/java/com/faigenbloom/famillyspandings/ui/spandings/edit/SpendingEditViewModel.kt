package com.faigenbloom.famillyspandings.ui.spandings.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.comon.SPENDING_ID_ARG
import com.faigenbloom.famillyspandings.comon.getCurrentDate
import com.faigenbloom.famillyspandings.comon.toLongDate
import com.faigenbloom.famillyspandings.domain.CalculateTotalUseCase
import com.faigenbloom.famillyspandings.domain.NormalizeDateUseCase
import com.faigenbloom.famillyspandings.domain.SaveSpendingUseCase
import com.faigenbloom.famillyspandings.domain.details.GetSpendingDetailsByIdUseCase
import com.faigenbloom.famillyspandings.domain.details.SaveDetailsUseCase
import com.faigenbloom.famillyspandings.domain.spendings.DeleteSpendingUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetSpendingUseCase
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import com.faigenbloom.famillyspandings.ui.spandings.DetailUiData
import com.faigenbloom.famillyspandings.ui.spandings.SpendingDetailListWrapper
import com.faigenbloom.famillyspandings.ui.spandings.SpendingUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpendingEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val normalizeDateUseCase: NormalizeDateUseCase,
    private val saveSpendingUseCase: SaveSpendingUseCase<SpendingUiData>,
    private val getSpendingDetailsUseCase: GetSpendingDetailsByIdUseCase<DetailUiData>,
    private val saveDetailsUseCase: SaveDetailsUseCase<DetailUiData>,
    private val getSpendingUseCase: GetSpendingUseCase<SpendingUiData>,
    private val deleteSpendingUseCase: DeleteSpendingUseCase,
    private val calculateTotalUseCase: CalculateTotalUseCase,
) : ViewModel() {
    private var spendingId: String = savedStateHandle[SPENDING_ID_ARG] ?: ""
    private var namingText: String = ""
    private var amountText: String = ""
    private var dateText: String = ""
    private var photoUri: Uri? = null
    private var detailsList: List<DetailUiData> = emptyList()
    private var isCategoriesOpened: Boolean = true
    private var isManualTotal: Boolean = false
    private var isHidden: Boolean = false
    private var isPlanned: Boolean = false
    private var isDuplicate: Boolean = false
    private var canDuplicate: Boolean = false
    private var selectedCategory: CategoryUiData? = null

    var onNext: (String) -> Unit = {}
    var onShowMessage: (MessageTypes) -> Unit = {}
    var onCategoryIdLoaded: (categoryId: String) -> Unit = {}
    var onScreenTransition: (isCategoriesOpened: Boolean) -> Unit = {}
    fun onCategorySelected(category: CategoryUiData) {
        selectedCategory = category
    }

    fun updateDetail(detailsUpdate: SpendingDetailListWrapper?) {
        detailsUpdate?.let {
            detailsList = it.details
            updateTotal()
            updateUI()
        }
    }

    private fun onSave() {
        if (checkAllFilled()) {
            viewModelScope.launch {
                selectedCategory?.id?.let { categoryId ->
                    spendingId = saveSpendingUseCase(
                        SpendingUiData(
                            id = spendingId,
                            name = namingText,
                            amount = amountText,
                            date = normalizeDateUseCase(dateText),
                            categoryId = categoryId,
                            photoUri = photoUri,
                            isManualTotal = isManualTotal,
                            isHidden = isHidden,
                            isPlanned = isPlanned,
                            isDuplicate = false,
                        ),
                    )
                    saveDetailsUseCase(
                        spendingId = spendingId,
                        details = detailsList,
                    )

                    state.onNext(spendingId)
                    onShowMessage(MessageTypes.SAVED)
                }
            }
        }
    }

    private fun checkAllFilled(): Boolean {
        if (namingText.isBlank() || amountText.isBlank() || selectedCategory == null) {
            return false
        }
        return true
    }

    private fun updateTotal() {
        amountText = calculateTotalUseCase(isManualTotal, detailsList, amountText)
    }

    private fun onDuplicate() {
        if (canDuplicate) {
            if (checkAllFilled()) {
                viewModelScope.launch {
                    selectedCategory?.id?.let { categoryId ->
                        spendingId = saveSpendingUseCase(
                            SpendingUiData(
                                id = "",
                                name = namingText,
                                amount = amountText,
                                date = normalizeDateUseCase(dateText),
                                categoryId = categoryId,
                                photoUri = photoUri,
                                isHidden = isHidden,
                                isPlanned = isPlanned,
                                isManualTotal = isManualTotal,
                                isDuplicate = true,
                            ),
                        )
                        saveDetailsUseCase(
                            spendingId = spendingId,
                            details = detailsList,
                        )
                        reload()
                    }
                }
            }
        }
    }

    private fun onDateChanged(date: String) {
        if (date.isNotBlank()) {
            dateText = date
            isPlanned = isPlanned || date.toLongDate() > getCurrentDate()
            updateUI()
        }
    }

    private fun onPhotoUriChanged(photoUri: Uri?) {
        this.photoUri = photoUri
        updateUI()
    }

    private fun deleteSpending() {
        viewModelScope.launch {
            deleteSpendingUseCase(spendingId)
            onNext("")
        }
    }

    private fun onPageChanged(isCategoriesOpened: Boolean) {
        this.isCategoriesOpened = isCategoriesOpened
        onScreenTransition(isCategoriesOpened)
        updateUI()
    }

    private fun onPlannedChanged() {
        isPlanned = !isPlanned
        updateUI()
    }

    private fun onNamingTextChanged(name: String) {
        this.namingText = name
        updateUI()
    }

    private fun onAmountTextChanged(amount: String) {
        amountText = amount
        isManualTotal = amount.isNotBlank()
        updateUI()
    }

    private fun onHideChanged() {
        isHidden = !isHidden
        onShowMessage(
            if (isHidden) {
                MessageTypes.SHOWN
            } else {
                MessageTypes.HIDEN
            },
        )
        updateUI()
    }

    private val state: SpendingEditState
        get() = SpendingEditState(
            spendingId = spendingId,
            isCategoriesOpened = isCategoriesOpened,
            namingText = namingText,
            amountText = amountText,
            canDuplicate = canDuplicate,
            detailsList = detailsList,
            dateText = dateText,
            isOkActive = checkAllFilled(),
            isHidden = isHidden,
            isDuplicate = isDuplicate,
            isPlanned = isPlanned,
            onHideChanged = ::onHideChanged,
            onPageChanged = ::onPageChanged,
            onNamingTextChanged = ::onNamingTextChanged,
            onAmountTextChanged = ::onAmountTextChanged,
            photoUri = photoUri,
            deleteSpending = ::deleteSpending,
            onPhotoUriChanged = ::onPhotoUriChanged,
            onSave = ::onSave,
            onPlannedChanged = ::onPlannedChanged,
            onDateChanged = ::onDateChanged,
            onDuplicate = ::onDuplicate,
            onNext = onNext,
        )

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow()
        .apply {
            reload()
        }

    private fun reload() {
        viewModelScope.launch {
            if (spendingId.isNotBlank()) {
                val spendingUiData = getSpendingUseCase(spendingId)
                namingText = spendingUiData.name
                amountText = spendingUiData.amount
                dateText = spendingUiData.date
                photoUri = spendingUiData.photoUri
                isHidden = spendingUiData.isHidden
                isPlanned = spendingUiData.isPlanned
                isManualTotal = spendingUiData.isManualTotal
                detailsList = getSpendingDetailsUseCase(spendingId)
                isDuplicate = spendingUiData.isDuplicate
                onCategoryIdLoaded(spendingUiData.categoryId)
                canDuplicate = spendingId.isNotBlank() && spendingUiData.isDuplicate.not()

                updateTotal()
                updateUI()
            }
        }
    }

    private fun updateUI() {
        _stateFlow.update { state }
    }

}

data class SpendingEditState(
    val spendingId: String,
    val canDuplicate: Boolean,
    val isCategoriesOpened: Boolean,
    val isDuplicate: Boolean,
    val onPageChanged: (Boolean) -> Unit,
    val namingText: String,
    val amountText: String,
    val dateText: String,
    var photoUri: Uri?,
    val detailsList: List<DetailUiData>,
    val isOkActive: Boolean,
    val isPlanned: Boolean,
    val isHidden: Boolean,
    val onNamingTextChanged: (String) -> Unit,
    val onAmountTextChanged: (String) -> Unit,
    val onPhotoUriChanged: (photoUri: Uri?) -> Unit,
    val onDateChanged: (String) -> Unit,
    val onPlannedChanged: () -> Unit,
    val onSave: () -> Unit,
    val onHideChanged: () -> Unit,
    val deleteSpending: () -> Unit,
    val onDuplicate: () -> Unit,
    val onNext: (String) -> Unit,
)


enum class MessageTypes {
    SAVED,
    HIDEN,
    SHOWN,
}
