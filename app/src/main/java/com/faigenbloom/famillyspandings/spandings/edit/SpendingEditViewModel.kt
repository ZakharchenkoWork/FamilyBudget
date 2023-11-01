package com.faigenbloom.famillyspandings.spandings.edit

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.categories.CategoriesRepository
import com.faigenbloom.famillyspandings.categories.CategoriesState
import com.faigenbloom.famillyspandings.categories.CategoriesViewModel
import com.faigenbloom.famillyspandings.comon.toLocalDate
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class SpendingEditViewModel(
    private val spendingsRepository: SpendingsRepository,
    private val categoriesRepository: CategoriesRepository,
) : CategoriesViewModel(categoriesRepository) {
    private var spendingId: String = ""
    private var namingText: String = ""
    private var amountText: String = ""
    private var dateText: String = ""
    private var photoUri: Uri? = null
    private var detailsList: List<SpendingDetail> = emptyList()
    private var isCategoriesOpened: Boolean = true
    private var isCalendarOpen: Boolean = false
    private var isManualTotal: Boolean = false

    private val onDetailAmountChanged: (Int, String) -> Unit = { detailIndex, amount ->
        detailsList = ArrayList(detailsList)
            .apply {
                set(detailIndex, get(detailIndex).copy(amount = amount))
            }
        amountText = updateTotal()
        _spendingEditStateFlow.update { spendingEditState }
    }
    private val onSave: () -> Unit = {
        viewModelScope.launch {
            spendingsRepository.saveSpending(
                SpendingEntity(
                    id = spendingId,
                    name = namingText,
                    amount = amountText.toDatabaseLong(),
                    date = dateText.toLocalDate(),
                    category = getSelectedCategory(),
                    photoUri = photoUri,
                    details = detailsList,
                ),
            )
        }
    }

    private fun updateTotal(): String {
        if (!isManualTotal) {
            var total = 0.0
            detailsList.forEach { total += it.amount.toDouble() }
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
    private val onPhotoUriChanged: (photoUri: Uri?) -> Unit = {
        photoUri = it
        _spendingEditStateFlow.update { spendingEditState }
    }
    private val onCalendarVisibilityChanged: (Boolean) -> Unit = {
        isCalendarOpen = it
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
            categoryState = categoriesStateFlow.value,
            isCategoriesOpened = isCategoriesOpened,
            onPageChanged = onPageChanged,
            namingText = namingText,
            amountText = updateTotal(),
            detailsList = detailsList,
            dateText = dateText,
            isCalendarOpen = isCalendarOpen,
            onCalendarVisibilityChanged = onCalendarVisibilityChanged,
            onNamingTextChanged = onNamingTextChanged,
            onAmountTextChanged = onAmountTextChanged,
            onAddNewDetail = onAddNewDetail,
            onDetailNameChanged = onDetailNameChanged,
            onDetailAmountChanged = onDetailAmountChanged,
            photoUri = photoUri,
            onPhotoUriChanged = onPhotoUriChanged,
            onSave = onSave,
        )

    private val _spendingEditStateFlow = MutableStateFlow(spendingEditState)
    val spendingEditStateFlow = _spendingEditStateFlow.asStateFlow()
        .apply {
            viewModelScope.launch {
                categoriesStateFlow.collectLatest {
                    _spendingEditStateFlow.update { spendingEditState }
                }
                detailsList = spendingsRepository.getDetails(spendingId)
            }
        }
}

data class SpendingEditState(
    val categoryState: CategoriesState,
    val isCategoriesOpened: Boolean,
    val onPageChanged: (Boolean) -> Unit,
    val namingText: String,
    val amountText: String,
    val dateText: String,
    var photoUri: Uri?,
    val detailsList: List<SpendingDetail>,
    val isCalendarOpen: Boolean,
    val onCalendarVisibilityChanged: (Boolean) -> Unit,
    val onNamingTextChanged: (String) -> Unit,
    val onAmountTextChanged: (String) -> Unit,
    val onAddNewDetail: () -> Unit,
    val onDetailNameChanged: (Int, String) -> Unit,
    val onDetailAmountChanged: (Int, String) -> Unit,
    val onPhotoUriChanged: (photoUri: Uri?) -> Unit,
    val onSave: () -> Unit,
)

data class SpendingDetail(val id: String, val name: String, val amount: String)

fun String.toDatabaseLong(): Long {
    return (this.toDouble() * 100).toLong()
}

@SuppressLint("SimpleDateFormat")
fun String.toDatabaseTime() =
    SimpleDateFormat("dd.MM.yyyy").parse(this)?.time ?: System.currentTimeMillis()
