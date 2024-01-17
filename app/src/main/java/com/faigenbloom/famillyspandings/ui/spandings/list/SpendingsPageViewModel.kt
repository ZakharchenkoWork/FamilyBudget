package com.faigenbloom.famillyspandings.ui.spandings.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.comon.Pattern
import com.faigenbloom.famillyspandings.comon.PlatesSorter
import com.faigenbloom.famillyspandings.comon.toLocalDate
import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.domain.categories.GetCategoryByIdUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetAllSpendingsUseCase
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import com.faigenbloom.famillyspandings.ui.spandings.SpendingUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpendingsPageViewModel(
    private val getAllSpendingsUseCase: GetAllSpendingsUseCase<SpendingUiData>,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase<CategoryUiData>,
) : ViewModel() {
    private var lastSpendings: List<SpendingCategoryUiData> = emptyList()
    private val sorter = PlatesSorter<SpendingCategoryUiData>()
    private var spendings: List<List<Pattern<SpendingCategoryUiData>>> = emptyList()
    private var isPlanned: Boolean = false
    private var isLoading: Boolean = true
    private fun onPlannedSwitched() {
        isPlanned = isPlanned.not()
        reloadData()
    }

    private val spendingsState: SpendingsState
        get() = SpendingsState(
            spendings,
            isPlannedListShown = isPlanned,
            isLoading = isLoading,
            onPlannedSwitched = ::onPlannedSwitched,
        )

    private val _spendingsStateFlow = MutableStateFlow(spendingsState)
    val spendingsStateFlow = _spendingsStateFlow.asStateFlow().apply {
        reloadData()
    }

    fun reloadData() {
        isLoading = true
        updateUI()
        viewModelScope.launch(Dispatchers.IO) {
            val spendingsUpdatedList = getAllSpendingsUseCase(isPlanned).map {
                it.toSpendingData(getCategoryByIdUseCase(it.categoryId))
            }
            if (lastSpendings != spendingsUpdatedList) {
                lastSpendings = spendingsUpdatedList
                spendings = sorter.prepareByDates(
                    lastSpendings,
                ).sortedByDescending {
                    it[0].getSortableDate()
                }.map {
                    sorter.findPattern(
                        sorter.preparePlatesSizes(it),
                    )
                }
            }
            isLoading = false
            updateUI()
        }
    }

    private fun updateUI() {
        _spendingsStateFlow.update { spendingsState }
    }
}

data class SpendingsState(
    val spendings: List<List<Pattern<SpendingCategoryUiData>>>,
    val isPlannedListShown: Boolean,
    val isLoading: Boolean,
    val onPlannedSwitched: (() -> Unit),
)

fun SpendingUiData.toSpendingData(category: CategoryUiData): SpendingCategoryUiData {
    return SpendingCategoryUiData(
        id = id,
        name = name,
        category = category,
        amount = amount.toLongMoney(),
        date = date.toLocalDate(),
        isHidden = isHidden,
    )
}
