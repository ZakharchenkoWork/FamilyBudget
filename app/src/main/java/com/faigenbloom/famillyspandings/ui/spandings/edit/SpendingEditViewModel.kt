package com.faigenbloom.famillyspandings.ui.spandings.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.comon.SPENDING_ID_ARG
import com.faigenbloom.famillyspandings.domain.CalculateTotalUseCase
import com.faigenbloom.famillyspandings.domain.GenerateIdUseCase
import com.faigenbloom.famillyspandings.domain.NormalizeDateUseCase
import com.faigenbloom.famillyspandings.domain.SaveSpendingUseCase
import com.faigenbloom.famillyspandings.domain.details.GetSpendingDetailsByIdUseCase
import com.faigenbloom.famillyspandings.domain.details.SaveDetailsUseCase
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
    private val idGeneratorUseCase: GenerateIdUseCase,
    private val normalizeDateUseCase: NormalizeDateUseCase,
    private val saveSpendingUseCase: SaveSpendingUseCase<SpendingUiData>,
    private val getSpendingDetailsUseCase: GetSpendingDetailsByIdUseCase<DetailUiData>,
    private val saveDetailsUseCase: SaveDetailsUseCase<DetailUiData>,
    private val getSpendingUseCase: GetSpendingUseCase<SpendingUiData>,
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
            if (detailsUpdate.details.isNotEmpty()) {
                detailsList = it.details
            } else {
                detailsList = emptyList()
            }
            amountText = updateTotal()
            updateUI()
        }
    }

    private fun onSave() {
        if (checkAllFilled()) {
            viewModelScope.launch {
                selectedCategory?.id?.let { categoryId ->
                    val spendingId = saveSpendingUseCase(
                        SpendingUiData(
                            id = idGeneratorUseCase(spendingId),
                            name = namingText,
                            amount = amountText,
                            date = normalizeDateUseCase(dateText),
                            categoryId = categoryId,
                            photoUri = photoUri,
                            isHidden = isHidden,
                            isPlanned = isHidden,
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
        if (namingText.isEmpty() || amountText.isEmpty() || selectedCategory == null) {
            return false
        }
        return true
    }

    private fun updateTotal(): String {
        amountText = calculateTotalUseCase(isManualTotal, detailsList, amountText)
        return amountText
    }

    private fun onDateChanged(date: String) {
        if (date.isNotEmpty()) {
            dateText = date
            updateUI()
        }
    }

    private fun onPhotoUriChanged(photoUri: Uri?) {
        this.photoUri = photoUri
        updateUI()
    }

    private fun onPageChanged(isCategoriesOpened: Boolean) {
        this.isCategoriesOpened = isCategoriesOpened
        onScreenTransition(isCategoriesOpened)
        updateUI()
    }

    private fun onNamingTextChanged(name: String) {
        this.namingText = name
        updateUI()
    }

    private fun onAmountTextChanged(amount: String) {
        amountText = amount
        isManualTotal = amount.isNotEmpty()
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
            amountText = updateTotal(),
            detailsList = detailsList,
            dateText = dateText,
            isOkActive = checkAllFilled(),
            isHidden = isHidden,
            onHideChanged = ::onHideChanged,
            onPageChanged = ::onPageChanged,
            onNamingTextChanged = ::onNamingTextChanged,
            onAmountTextChanged = ::onAmountTextChanged,
            photoUri = photoUri,
            onPhotoUriChanged = ::onPhotoUriChanged,
            onSave = ::onSave,
            onDateChanged = ::onDateChanged,
            onNext = onNext,
        )

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow()
        .apply {

            viewModelScope.launch {
                if (spendingId.isNotEmpty()) {
                    val spendingUiData = getSpendingUseCase(spendingId)
                    namingText = spendingUiData.name
                    amountText = spendingUiData.amount
                    dateText = spendingUiData.date
                    photoUri = spendingUiData.photoUri
                    isHidden = spendingUiData.isHidden
                    detailsList = getSpendingDetailsUseCase(spendingId)
                    onCategoryIdLoaded(spendingUiData.categoryId)

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
    val isCategoriesOpened: Boolean,
    val onPageChanged: (Boolean) -> Unit,
    val namingText: String,
    val amountText: String,
    val dateText: String,
    var photoUri: Uri?,
    val detailsList: List<DetailUiData>,
    val isOkActive: Boolean,
    val isHidden: Boolean,
    val onNamingTextChanged: (String) -> Unit,
    val onAmountTextChanged: (String) -> Unit,
    val onPhotoUriChanged: (photoUri: Uri?) -> Unit,
    val onDateChanged: (String) -> Unit,
    val onSave: () -> Unit,
    val onHideChanged: () -> Unit,
    val onNext: (String) -> Unit,
)


enum class MessageTypes {
    SAVED,
    HIDEN,
    SHOWN,
}
