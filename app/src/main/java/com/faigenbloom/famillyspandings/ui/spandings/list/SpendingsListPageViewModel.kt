package com.faigenbloom.famillyspandings.ui.spandings.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.comon.toLocalDate
import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.domain.categories.GetCategoryByIdUseCase
import com.faigenbloom.famillyspandings.domain.spendings.Divider
import com.faigenbloom.famillyspandings.domain.spendings.GetAllSpendingsUseCase
import com.faigenbloom.famillyspandings.domain.spendings.Pattern
import com.faigenbloom.famillyspandings.domain.spendings.SortPlatesUseCase
import com.faigenbloom.famillyspandings.domain.spendings.dividers.DayGroupDivider
import com.faigenbloom.famillyspandings.domain.spendings.dividers.MonthGroupDivider
import com.faigenbloom.famillyspandings.domain.spendings.dividers.YearGroupDivider
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import com.faigenbloom.famillyspandings.ui.spandings.SpendingUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpendingsListPageViewModel(
    private val getAllSpendingsUseCase: GetAllSpendingsUseCase<SpendingUiData>,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase<CategoryUiData>,
    private val sortPlatesUseCase: SortPlatesUseCase<SpendingCategoryUiData>,
) : ViewModel() {
    private var lastSpendings: List<SpendingCategoryUiData> = emptyList()
    private var spendings: List<List<Pattern<SpendingCategoryUiData>>> = emptyList()
    private var isPlanned: Boolean = false
    private var isLoading: Boolean = true
    private var filterType: FilterType = FilterType.DAILY
    private fun onPlannedSwitched() {
        isPlanned = isPlanned.not()
        reloadData()
    }

    fun onDailyFiltered() {
        filterType = FilterType.DAILY
    }

    fun onMonthlyFiltered() {
        filterType = FilterType.MONTHLY
    }

    fun onYearlyFiltered() {
        filterType = FilterType.YEAR
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

    private fun getDividerForFilter(): Divider<SpendingCategoryUiData> {
        return when (filterType) {
            FilterType.DAILY -> DayGroupDivider()
            FilterType.MONTHLY -> MonthGroupDivider()
            FilterType.YEAR -> YearGroupDivider()
        }
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
                spendings = sortPlatesUseCase(getDividerForFilter(), lastSpendings)
            }
            isLoading = false
            updateUI()
        }
    }

    private fun updateUI() {
        _spendingsStateFlow.update { spendingsState }
    }

}

enum class FilterType {
    DAILY,
    MONTHLY,
    YEAR,
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
