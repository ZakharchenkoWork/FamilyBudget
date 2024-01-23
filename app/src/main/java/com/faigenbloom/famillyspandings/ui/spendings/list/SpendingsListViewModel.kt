package com.faigenbloom.famillyspandings.ui.spendings.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.common.toLocalDate
import com.faigenbloom.famillyspandings.common.toLongDate
import com.faigenbloom.famillyspandings.common.toLongMoney
import com.faigenbloom.famillyspandings.common.toReadableDate
import com.faigenbloom.famillyspandings.domain.categories.GetCategoryByIdUseCase
import com.faigenbloom.famillyspandings.domain.spendings.Divider
import com.faigenbloom.famillyspandings.domain.spendings.FilterType
import com.faigenbloom.famillyspandings.domain.spendings.GetAllSpendingsUseCase
import com.faigenbloom.famillyspandings.domain.spendings.Pattern
import com.faigenbloom.famillyspandings.domain.spendings.SortPlatesUseCase
import com.faigenbloom.famillyspandings.domain.spendings.dividers.DayGroupDivider
import com.faigenbloom.famillyspandings.domain.spendings.dividers.MonthGroupDivider
import com.faigenbloom.famillyspandings.domain.spendings.dividers.YearGroupDivider
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import com.faigenbloom.famillyspandings.ui.spendings.SpendingUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpendingsListViewModel(
    private val getAllSpendingsUseCase: GetAllSpendingsUseCase<SpendingUiData>,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase<CategoryUiData>,
    private val sortPlatesUseCase: SortPlatesUseCase<SpendingCategoryUiData>,
) : ViewModel() {
    private var lastSpendings: List<SpendingCategoryUiData> = emptyList()
    private var spendings: List<List<Pattern<SpendingCategoryUiData>>> = emptyList()
    private var isPlanned: Boolean = false
    private var isLoading: Boolean = true
    private var filterType: FilterType = FilterType.Daily()
    private var fromDate: Long = filterType.from
    private var toDate: Long = filterType.to

    var onCalendarRequested: (fromDate: String, toDate: String) -> Unit = { _, _ ->

    }

    val onDateRangeChanged: (fromDate: String, toDate: String) -> Unit = { fromDate, toDate ->
        if (fromDate.isNotBlank()) {
            this.fromDate = fromDate.toLongDate()
            this.toDate = toDate.ifBlank { fromDate }.toLongDate()
            lastSpendings = emptyList()
            reloadData()
        }
    }

    private fun onPlannedSwitched() {
        isPlanned = isPlanned.not()

        filterType = when (filterType) {
            is FilterType.Daily -> FilterType.Daily(isPlanned)
            is FilterType.Monthly -> FilterType.Monthly(isPlanned)
            is FilterType.Yearly -> FilterType.Yearly(isPlanned)
        }

        reloadData()
    }

    fun calendarRequest() {
        onCalendarRequested(fromDate.toReadableDate(), toDate.toReadableDate())
    }

    fun onDailyFiltered() {
        filterType = if (filterType is FilterType.Daily) {
            FilterType.Daily()
        } else {
            FilterType.Daily(fromDate, toDate)
        }
        lastSpendings = emptyList()
        reloadData()
    }

    fun onMonthlyFiltered() {
        filterType = if (filterType is FilterType.Monthly) {
            FilterType.Monthly()
        } else {
            FilterType.Monthly(fromDate, toDate)
        }
        lastSpendings = emptyList()
        reloadData()
    }

    fun onYearlyFiltered() {
        filterType = if (filterType is FilterType.Yearly) {
            FilterType.Yearly()
        } else {
            FilterType.Yearly(fromDate, toDate)
        }
        lastSpendings = emptyList()
        reloadData()
    }

    private val spendingsState: SpendingsState
        get() = SpendingsState(
            spendings,
            isPlannedListShown = isPlanned,
            isLoading = isLoading,
            filterType = filterType,
            onPlannedSwitched = ::onPlannedSwitched,
        )
    private val _spendingsStateFlow = MutableStateFlow(spendingsState)

    val spendingsStateFlow = _spendingsStateFlow.asStateFlow().apply {
        reloadData()
    }

    private fun getDividerForFilter(): Divider<SpendingCategoryUiData> {
        return when (filterType) {
            is FilterType.Daily -> DayGroupDivider(filterType.from, filterType.to)
            is FilterType.Monthly -> MonthGroupDivider(filterType.from, filterType.to)
            is FilterType.Yearly -> YearGroupDivider(filterType.from, filterType.to)
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

data class SpendingsState(
    val spendings: List<List<Pattern<SpendingCategoryUiData>>>,
    val isPlannedListShown: Boolean,
    val filterType: FilterType,
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
