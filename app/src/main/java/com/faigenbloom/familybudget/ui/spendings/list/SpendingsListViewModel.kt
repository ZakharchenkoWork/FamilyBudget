package com.faigenbloom.familybudget.ui.spendings.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.faigenbloom.familybudget.common.toLongDate
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.domain.spendings.DatedList
import com.faigenbloom.familybudget.domain.spendings.FilterType
import com.faigenbloom.familybudget.domain.spendings.SpendingsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class SpendingsListViewModel(
    private val spendingsPagingSource: SpendingsPagingSource,
) : ViewModel() {
    var onCalendarRequested: (fromDate: String, toDate: String) -> Unit = { _, _ ->
    }

    fun onDateRangeChanged(fromDate: String, toDate: String) {
        if (fromDate.isNotBlank()) {
            val filterType = state.filterType.copy(
                from = fromDate.toLongDate(),
                to = toDate.ifBlank { fromDate }.toLongDate(),
            )
            reloadData(filterType)
        }
    }

    private fun onRangeFiltered() {
        onCalendarRequested(
            state.filterType.from.toReadableDate(),
            state.filterType.to.toReadableDate(),
        )
    }

    private fun onDailyFiltered() {
        val filterType = if (state.filterType is FilterType.Daily) {
            FilterType.Daily(
                isPlanned = state.filterType.isPlanned,
            )
        } else {
            FilterType.Daily(
                from = state.filterType.from,
                to = state.filterType.to,
                isPlanned = state.filterType.isPlanned,
            )
        }
        reloadData(filterType)
    }

    private fun onMonthlyFiltered() {
        val filterType = if (state.filterType is FilterType.Monthly) {
            FilterType.Monthly(isPlanned = state.filterType.isPlanned)
        } else {
            FilterType.Monthly(
                from = state.filterType.from,
                to = state.filterType.to,
                isPlanned = state.filterType.isPlanned,
            )
        }
        reloadData(filterType)
    }

    private fun onYearlyFiltered() {
        val filterType = if (state.filterType is FilterType.Yearly) {
            FilterType.Yearly(isPlanned = state.filterType.isPlanned)
        } else {
            FilterType.Yearly(
                from = state.filterType.from,
                to = state.filterType.to,
                isPlanned = state.filterType.isPlanned,
            )
        }
        reloadData(filterType)
    }

    private fun onPlannedSwitched() {
        val filterType = state.filterType.copy(
            isPlanned = state.filterType.isPlanned.not(),
        )
        reloadData(filterType)
    }

    private val state: SpendingsState
        get() = _stateFlow.value
    private val _stateFlow = MutableStateFlow(
        SpendingsState(
            onPlannedSwitched = ::onPlannedSwitched,
            onRangeFiltered = ::onRangeFiltered,
            onDailyFiltered = ::onDailyFiltered,
            onMonthlyFiltered = ::onMonthlyFiltered,
            onYearlyFiltered = ::onYearlyFiltered,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        reloadData()
    }

    private fun reloadData(filterType: FilterType = state.filterType) {
        state.isLoading.value = true
        _stateFlow.update { state ->
            state.copy(
                spendingsPager = Pager(
                    pagingSourceFactory = { spendingsPagingSource },
                    initialKey = filterType,
                    config = pagingConfig,
                ).flow.cachedIn(viewModelScope),
                filterType = filterType,
            )
        }
        state.isLoading.value = false
    }
}

data class SpendingsState(
    val spendingsPager: Flow<PagingData<DatedList>> = flowOf(PagingData.empty()),
    val filterType: FilterType = FilterType.Daily(false),
    val isLoading: MutableState<Boolean> = mutableStateOf(true),
    val onPlannedSwitched: (() -> Unit) = {},
    val onRangeFiltered: (() -> Unit) = {},
    val onDailyFiltered: (() -> Unit) = {},
    val onMonthlyFiltered: (() -> Unit) = {},
    val onYearlyFiltered: (() -> Unit) = {},
)

val pagingConfig = PagingConfig(
    pageSize = 1,
    prefetchDistance = 1,
    enablePlaceholders = false,
    initialLoadSize = 1,
)
