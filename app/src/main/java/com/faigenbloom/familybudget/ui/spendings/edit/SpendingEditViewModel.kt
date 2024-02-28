package com.faigenbloom.familybudget.ui.spendings.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.common.SPENDING_ID_ARG
import com.faigenbloom.familybudget.common.getCurrentDate
import com.faigenbloom.familybudget.common.toLongDate
import com.faigenbloom.familybudget.common.toNormalizedMoney
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.domain.CalculateTotalUseCase
import com.faigenbloom.familybudget.domain.NormalizeDateUseCase
import com.faigenbloom.familybudget.domain.currency.GetChosenCurrencyUseCase
import com.faigenbloom.familybudget.domain.details.GetSpendingDetailsByIdUseCase
import com.faigenbloom.familybudget.domain.details.SaveDetailsUseCase
import com.faigenbloom.familybudget.domain.family.GetPersonNameUseCase
import com.faigenbloom.familybudget.domain.spendings.DeleteSpendingUseCase
import com.faigenbloom.familybudget.domain.spendings.GetSpendingUseCase
import com.faigenbloom.familybudget.domain.spendings.SaveSpendingUseCase
import com.faigenbloom.familybudget.ui.categories.CategoryUiData
import com.faigenbloom.familybudget.ui.spendings.DetailUiData
import com.faigenbloom.familybudget.ui.spendings.SpendingDetailListWrapper
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

class SpendingEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val normalizeDateUseCase: NormalizeDateUseCase,
    private val saveSpendingUseCase: SaveSpendingUseCase<SpendingUiData>,
    private val saveDetailsUseCase: SaveDetailsUseCase<DetailUiData>,
    private val getSpendingDetailsUseCase: GetSpendingDetailsByIdUseCase<DetailUiData>,
    private val getSpendingUseCase: GetSpendingUseCase<SpendingUiData>,
    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,
    private val deleteSpendingUseCase: DeleteSpendingUseCase,
    private val calculateTotalUseCase: CalculateTotalUseCase,
    private val getPersonNameUseCase: GetPersonNameUseCase,
    private val idSource: IdSource,
) : ViewModel() {
    private var spendingId: String = savedStateHandle[SPENDING_ID_ARG] ?: ""
    private var selectedCategory: CategoryUiData? = null
    private var ownerId: String = ""
    private var isManualTotal: Boolean = false
    private var canDuplicate: Boolean = false

    var onNext: (String) -> Unit = {}
    var onCategoryIdLoaded: (categoryId: String) -> Unit = {}
    var onScreenTransition: (isCategoriesOpened: Boolean) -> Unit = {}
    fun onCategorySelected(category: CategoryUiData) {
        selectedCategory = category
    }

    fun updateDetail(detailsUpdate: SpendingDetailListWrapper?) {
        detailsUpdate?.let { update ->
            _stateFlow.update {
                state.copy(
                    detailsList = update.details,
                    amountText = updateTotal(update.details),
                )
            }
        }
    }

    private fun onSave() {
        if (checkAndShowErrors()) {
            viewModelScope.launch {
                selectedCategory?.id?.let { categoryId ->
                    spendingId = saveSpendingUseCase(
                        SpendingUiData(
                            id = state.spendingId,
                            name = state.namingText,
                            amount = state.amountText,
                            date = normalizeDateUseCase(state.dateText),
                            categoryId = categoryId,
                            photoUri = state.photoUri,
                            isManualTotal = isManualTotal,
                            isHidden = state.isHidden,
                            isPlanned = state.isPlanned,
                            isDuplicate = false,
                            ownerId = ownerId.ifBlank { idSource[ID.USER] },
                        ),
                    )
                    saveDetailsUseCase(
                        spendingId = spendingId,
                        details = state.detailsList,
                    )

                    onNext(spendingId)
                }
            }
        }
    }

    private fun checkAllFilled(): Boolean {
        if (state.namingText.isBlank() || state.amountText.isBlank() || selectedCategory == null) {
            return false
        }
        return true
    }

    private fun checkAndShowErrors(): Boolean {
        _stateFlow.update {
            state.copy(
                isNameError = state.namingText.isBlank(),
                isAmountError = state.amountText.isBlank(),
                isCategoryError = state.namingText.isBlank().not() && state.amountText.isBlank()
                    .not() && selectedCategory == null,
                isInfoOpened = true,
            )
        }
        if (state.isCategoryError) {
            onPageChanged(false)
        }
        return checkAllFilled()
    }

    private fun onResetErrors() {
        _stateFlow.update {
            state.copy(
                isNameError = false,
                isAmountError = false,
                isCategoryError = false,
            )
        }
    }

    private fun updateTotal(detailsList: List<DetailUiData>): String {
        return calculateTotalUseCase(isManualTotal, detailsList, state.amountText)
    }

    private fun onDuplicate() {
        if (canDuplicate) {
            if (checkAndShowErrors()) {
                viewModelScope.launch {
                    selectedCategory?.id?.let { categoryId ->
                        spendingId = saveSpendingUseCase(
                            SpendingUiData(
                                id = "",
                                name = state.namingText,
                                amount = state.amountText,
                                date = normalizeDateUseCase(state.dateText),
                                categoryId = categoryId,
                                photoUri = state.photoUri,
                                isHidden = state.isHidden,
                                isPlanned = state.isPlanned,
                                isManualTotal = isManualTotal,
                                isDuplicate = true,
                                ownerId = ownerId,
                            ),
                        )
                        saveDetailsUseCase(
                            spendingId = spendingId,
                            details = state.detailsList,
                            isDuplicate = true,
                        )
                        reload()
                    }
                }
            }
        }
    }

    private fun onDateChanged(date: String) {
        if (date.isNotBlank()) {
            _stateFlow.update {
                state.copy(
                    dateText = date,
                    isPlanned = state.isPlanned || date.toLongDate() > getCurrentDate(),
                )
            }
        }
    }

    private fun onPhotoUriChanged(photoUri: Uri?) {
        _stateFlow.update {
            state.copy(
                photoUri = photoUri,
            )
        }

    }

    private fun deleteSpending() {
        viewModelScope.launch {
            deleteSpendingUseCase(spendingId)
            onNext("")
        }
    }

    private fun onPageChanged(isInfoOpened: Boolean) {
        _stateFlow.update {
            state.copy(
                isInfoOpened = isInfoOpened,
            )
        }
        onScreenTransition(isInfoOpened)
    }

    private fun onPlannedChanged() {
        _stateFlow.update {
            state.copy(
                isPlanned = !state.isPlanned,
            )
        }
    }

    private fun onNamingTextChanged(name: String) {
        _stateFlow.update {
            state.copy(
                namingText = name,
            )
        }
    }

    private fun onAmountTextChanged(amount: String) {
        _stateFlow.update {
            state.copy(
                amountText = amount.toNormalizedMoney(),
            )
        }
        isManualTotal = amount.isNotBlank()

    }

    private fun onHideChanged() {
        _stateFlow.update {
            state.copy(
                isHidden = !state.isHidden,
            )
        }
    }


    private val _stateFlow = MutableStateFlow(
        SpendingEditState(
            onResetErrors = ::onResetErrors,
            onHideChanged = ::onHideChanged,
            onPageChanged = ::onPageChanged,
            onNamingTextChanged = ::onNamingTextChanged,
            onAmountTextChanged = ::onAmountTextChanged,
            deleteSpending = ::deleteSpending,
            onPhotoUriChanged = ::onPhotoUriChanged,
            onSave = ::onSave,
            onPlannedChanged = ::onPlannedChanged,
            onDateChanged = ::onDateChanged,
            onDuplicate = ::onDuplicate,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow()
        .apply {
            reload()
        }
    private val state: SpendingEditState
        get() = _stateFlow.value

    private fun reload() {
        viewModelScope.launch {
            if (spendingId.isNotBlank()) {
                val spendingUiData = getSpendingUseCase(spendingId)
                isManualTotal = spendingUiData.isManualTotal
                ownerId = spendingUiData.ownerId
                onCategoryIdLoaded(spendingUiData.categoryId)
                _stateFlow.update {
                    state.copy(
                        spendingId = spendingId,
                        namingText = spendingUiData.name,
                        amountText = spendingUiData.amount,
                        dateText = spendingUiData.date,
                        photoUri = spendingUiData.photoUri,
                        isHidden = spendingUiData.isHidden,
                        isPlanned = spendingUiData.isPlanned,
                        detailsList = getSpendingDetailsUseCase(spendingId),
                        isDuplicate = spendingUiData.isDuplicate,
                        canDuplicate = spendingId.isNotBlank() && spendingUiData.isDuplicate.not(),
                        currency = getChosenCurrencyUseCase(),
                        ownerName = getPersonNameUseCase(spendingUiData.ownerId),
                        isCurrentUserOwner = ownerId == idSource[ID.USER],
                    )
                }
                onCategoryIdLoaded(spendingUiData.categoryId)
                _stateFlow.update {
                    state.copy(
                        isOkActive = checkAllFilled(),
                        amountText = updateTotal(it.detailsList),
                    )
                }
            }
        }
    }
}

data class SpendingEditState(
    val spendingId: String = "",
    val canDuplicate: Boolean = false,
    val isInfoOpened: Boolean = true,
    val isDuplicate: Boolean = false,
    val onPageChanged: (Boolean) -> Unit,
    val namingText: String = "",
    val amountText: String = "",
    val ownerName: String = "",
    val isCurrentUserOwner: Boolean = false,
    val isNameError: Boolean = false,
    val isAmountError: Boolean = false,
    val isCategoryError: Boolean = false,
    val dateText: String = "",
    var photoUri: Uri? = null,
    val detailsList: List<DetailUiData> = emptyList(),
    val isOkActive: Boolean = false,
    val isPlanned: Boolean = false,
    val isHidden: Boolean = false,
    val currency: Currency = Currency.getInstance(Locale.getDefault()),
    val onNamingTextChanged: (String) -> Unit,
    val onAmountTextChanged: (String) -> Unit,
    val onPhotoUriChanged: (photoUri: Uri?) -> Unit,
    val onDateChanged: (String) -> Unit,
    val onPlannedChanged: () -> Unit,
    val onResetErrors: () -> Unit,
    val onSave: () -> Unit,
    val onHideChanged: () -> Unit,
    val deleteSpending: () -> Unit,
    val onDuplicate: () -> Unit,
)
