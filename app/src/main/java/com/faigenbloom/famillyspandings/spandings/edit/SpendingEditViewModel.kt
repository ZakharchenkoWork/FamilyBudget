package com.faigenbloom.famillyspandings.spandings.edit

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.categories.CategoriesRepository
import com.faigenbloom.famillyspandings.categories.CategoriesState
import com.faigenbloom.famillyspandings.categories.CategoriesViewModel
import com.faigenbloom.famillyspandings.comon.SPENDING_ID_ARG
import com.faigenbloom.famillyspandings.comon.checkOrGenId
import com.faigenbloom.famillyspandings.comon.toReadableDate
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class SpendingEditViewModel(
    savedStateHandle: SavedStateHandle,
    categoriesRepository: CategoriesRepository,
    private val spendingsRepository: SpendingsRepository,
) : CategoriesViewModel(categoriesRepository) {
    private var spendingId: String = savedStateHandle[SPENDING_ID_ARG] ?: ""
    private var namingText: String = ""
    private var amountText: String = ""
    private var dateText: String = ""
    private var photoUri: Uri? = null
    private var detailsList: List<SpendingDetail> = emptyList()
    private var isCategoriesOpened: Boolean = true
    private var isManualTotal: Boolean = false

    var onNext: (String) -> Unit = {}

    private val onDetailAmountChanged: (Int, String) -> Unit = { detailIndex, amount ->
        detailsList = ArrayList(detailsList)
            .apply {
                set(detailIndex, get(detailIndex).copy(amount = amount))
            }
        amountText = updateTotal()
        _spendingEditStateFlow.update { spendingEditState }
    }

    private val onSave: () -> Unit = {
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
                )
                spendingEditState.onNext(spendingId)
            }
        }
    }

    private fun checkAllFilled(): Boolean {
        if (namingText.isEmpty() || dateText.isEmpty() || amountText.isEmpty() || isCategorySelected.not()) {
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

    private val onAddNewDetail: () -> Unit = {
        detailsList = ArrayList(detailsList)
            .apply { add(SpendingDetail("", "", "")) }
        _spendingEditStateFlow.update { spendingEditState }
    }

    private val onDetailNameChanged: (Int, String) -> Unit = { detailIndex, name ->
        detailsList = detailsList.mapIndexed { index, spendingDetail ->
            if (detailIndex == index) {
                spendingDetail.copy(name = name)
            } else {
                spendingDetail
            }
        }
        _spendingEditStateFlow.update { spendingEditState }
    }
    private val onDateChanged: (String) -> Unit = {
        if (it.isNotEmpty()) {
            dateText = it
            _spendingEditStateFlow.update { spendingEditState }
        }
    }
    private val onPhotoUriChanged: (photoUri: Uri?) -> Unit = {
        photoUri = it
        _spendingEditStateFlow.update { spendingEditState }
    }

    private var onPageChanged: (Boolean) -> Unit = {
        isCategoriesOpened = it
        _spendingEditStateFlow.update { spendingEditState }
    }
    private var onNamingTextChanged: (String) -> Unit = {
        namingText = it
        _spendingEditStateFlow.update { spendingEditState }
    }
    private var onAmountTextChanged: (String) -> Unit = {
        amountText = it
        isManualTotal = it.isNotEmpty()
        _spendingEditStateFlow.update { spendingEditState }
    }
    private val spendingEditState: SpendingEditState
        get() = SpendingEditState(
            spendingId = spendingId,
            categoryState = categoriesStateFlow.value,
            isCategoriesOpened = isCategoriesOpened,
            onPageChanged = onPageChanged,
            namingText = namingText,
            amountText = updateTotal(),
            detailsList = detailsList,
            dateText = dateText,
            isOkActive = checkAllFilled(),
            onNamingTextChanged = onNamingTextChanged,
            onAmountTextChanged = onAmountTextChanged,
            onAddNewDetail = onAddNewDetail,
            onDetailNameChanged = onDetailNameChanged,
            onDetailAmountChanged = onDetailAmountChanged,
            photoUri = photoUri,
            onPhotoUriChanged = onPhotoUriChanged,
            onSave = onSave,
            onNext = onNext,
            onDateChanged = onDateChanged,
        )

    private val _spendingEditStateFlow = MutableStateFlow(spendingEditState)
    val spendingEditStateFlow = _spendingEditStateFlow.asStateFlow()
        .apply {

            viewModelScope.launch {
                var spendingEntity: SpendingEntity? = null
                if (spendingId.isNotEmpty()) {
                    spendingEntity = spendingsRepository.getSpending(spendingId)
                    namingText = spendingEntity.name
                    amountText = spendingEntity.amount.toReadableMoney()
                    dateText = spendingEntity.date.toReadableDate()
                    photoUri = spendingEntity.photoUri?.let { Uri.parse(it) }

                    detailsList = spendingsRepository.getSpendingDetails(spendingId).map {
                        SpendingDetail.fromEntity(it)
                    }
                    _spendingEditStateFlow.update { spendingEditState }
                }
                categoriesStateFlow.collectLatest {
                    spendingEntity?.let {
                        setSelectedCategory(it.categoryId)
                    }
                    _spendingEditStateFlow.update { spendingEditState }
                }
            }
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
    val onNamingTextChanged: (String) -> Unit,
    val onAmountTextChanged: (String) -> Unit,
    val onAddNewDetail: () -> Unit,
    val onDetailNameChanged: (Int, String) -> Unit,
    val onDetailAmountChanged: (Int, String) -> Unit,
    val onPhotoUriChanged: (photoUri: Uri?) -> Unit,
    val onDateChanged: (String) -> Unit,
    val onSave: () -> Unit,
    val onNext: (String) -> Unit,
)

data class SpendingDetail(val id: String, val name: String, val amount: String) {
    fun mapToEntity(): SpendingDetailEntity {
        return SpendingDetailEntity(
            id = id.checkOrGenId(),
            name = name,
            amount = amount.toDatabaseLong(),
        )
    }

    companion object {
        fun fromEntity(entity: SpendingDetailEntity): SpendingDetail {
            return SpendingDetail(
                id = entity.id,
                name = entity.name,
                amount = entity.amount.toReadableMoney(),
            )
        }
    }
}

fun String.toDatabaseLong(): Long {
    return (this.toDouble() * 100).toLong()
}

@SuppressLint("SimpleDateFormat")
fun String.toDatabaseTime() =
    SimpleDateFormat("dd.MM.yyyy").parse(this)?.time ?: System.currentTimeMillis()
