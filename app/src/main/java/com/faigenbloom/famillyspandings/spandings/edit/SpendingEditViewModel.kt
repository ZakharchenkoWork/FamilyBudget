package com.faigenbloom.famillyspandings.spandings.edit

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.categories.CategoriesRepository
import com.faigenbloom.famillyspandings.categories.CategoriesState
import com.faigenbloom.famillyspandings.categories.CategoriesViewModel
import com.faigenbloom.famillyspandings.comon.SPENDING_ID_ARG
import com.faigenbloom.famillyspandings.comon.toReadableDate
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpendingEditViewModel(
    savedStateHandle: SavedStateHandle,
    categoriesRepository: CategoriesRepository,
    private val spendingsRepository: SpendingsEditRepository,
) : CategoriesViewModel(categoriesRepository) {
    private var spendingId: String = savedStateHandle[SPENDING_ID_ARG] ?: ""
    private var namingText: String = ""
    private var amountText: String = ""
    private var dateText: String = ""
    private var photoUri: Uri? = null
    private var detailsList: List<SpendingDetail> = emptyList()
    private var isCategoriesOpened: Boolean = true
    private var isManualTotal: Boolean = false
    private var isHidden: Boolean = false

    var onNext: (String) -> Unit = {}
    var onShowMessage: (MessageTypes) -> Unit = {}
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
                val spendingId = spendingsRepository.saveSpending(
                    id = spendingId,
                    name = namingText,
                    amount = amountText,
                    date = dateText,
                    category = getSelectedCategory(),
                    photoUri = photoUri,
                    details = detailsList.map { it.mapToEntity() },
                    isHidden = isHidden,
                )
                state.onNext(spendingId)
                onShowMessage(MessageTypes.SAVED)
            }
        }
    }

    private fun checkAllFilled(): Boolean {
        if (namingText.isEmpty() || amountText.isEmpty() || isCategorySelected.not()) {
            return false
        }
        return true
    }

    private fun updateTotal(): String {
        if (!isManualTotal && detailsList.isNotEmpty()) {
            var total = 0.0
            detailsList.forEach { total += if (it.amount.isNotEmpty()) it.amount.toDouble() else 0.0 }
            amountText = total.toString()
        }
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
            categoryState = categoriesStateFlow.value,
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

    private val _spendingEditStateFlow = MutableStateFlow(state)
    val spendingEditStateFlow = _spendingEditStateFlow.asStateFlow()
        .apply {

            viewModelScope.launch {
                var spendingEntity: SpendingEntity? = null
                if (spendingId.isNotEmpty()) {
                    spendingEntity = spendingsRepository.getSpending(spendingId)
                    namingText = spendingEntity.name
                    amountText = spendingEntity.amount.toReadableMoney()
                    dateText = spendingEntity.date.toReadableDate()
                    photoUri = spendingEntity.photoUri?.toUri()
                    isHidden = spendingEntity.isHidden
                    detailsList = spendingsRepository.getSpendingDetails(spendingId).map {
                        SpendingDetail.fromEntity(it)
                    }
                    updateUI()
                }
                categoriesStateFlow.collectLatest {
                    spendingEntity?.let {
                        setSelectedCategory(it.categoryId)
                    }
                    updateUI()
                }
            }
        }

    private fun updateUI() {
        _spendingEditStateFlow.update { state }
    }
}

data class SpendingEditState(
    val spendingId: String,
    val categoryState: CategoriesState,
    val isCategoriesOpened: Boolean,
    val onPageChanged: (Boolean) -> Unit,
    val namingText: String,
    val amountText: String,
    val dateText: String,
    var photoUri: Uri?,
    val detailsList: List<SpendingDetail>,
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
