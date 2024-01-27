package com.faigenbloom.famillyspandings.ui.spendings.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.faigenbloom.famillyspandings.common.toLongDate
import com.faigenbloom.famillyspandings.common.toReadableDate
import com.faigenbloom.famillyspandings.domain.spendings.DatedList
import com.faigenbloom.famillyspandings.domain.spendings.FilterType
import com.faigenbloom.famillyspandings.domain.spendings.SpendingsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class SpendingsListViewModel(
    private val spendingsPagingSource: SpendingsPagingSource,
) : ViewModel() {
    private var isPlanned: Boolean = false
    private var isLoading: Boolean = true
    private var filterType: FilterType = FilterType.Daily(isPlanned)
    private var fromDate: Long = filterType.from
    private var toDate: Long = filterType.to

    private var spendingsPager: Flow<PagingData<DatedList>> = flowOf(PagingData.empty())

    var onCalendarRequested: (fromDate: String, toDate: String) -> Unit = { _, _ ->

    }

    val onDateRangeChanged: (fromDate: String, toDate: String) -> Unit = { fromDate, toDate ->
        if (fromDate.isNotBlank()) {
            this.fromDate = fromDate.toLongDate()
            this.toDate = toDate.ifBlank { fromDate }.toLongDate()
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
            FilterType.Daily(isPlanned)
        } else {
            FilterType.Daily(fromDate, toDate, isPlanned)
        }
        reloadData()
    }

    fun onMonthlyFiltered() {
        filterType = if (filterType is FilterType.Monthly) {
            FilterType.Monthly(isPlanned)
        } else {
            FilterType.Monthly(fromDate, toDate, isPlanned)
        }
        reloadData()
    }

    fun onYearlyFiltered() {
        filterType = if (filterType is FilterType.Yearly) {
            FilterType.Yearly(isPlanned)
        } else {
            FilterType.Yearly(fromDate, toDate, isPlanned)
        }
        reloadData()
    }

    private val spendingsState: SpendingsState
        get() = SpendingsState(
            spendingsPager = spendingsPager,
            isPlannedListShown = isPlanned,
            isLoading = isLoading,
            filterType = filterType,
            onPlannedSwitched = ::onPlannedSwitched,
        )
    private val _spendingsStateFlow = MutableStateFlow(spendingsState)

    val spendingsStateFlow = _spendingsStateFlow.asStateFlow().apply {
        reloadData()
    }

    fun reloadData() {
        isLoading = true
        updateUI()
        spendingsPager = Pager(
            pagingSourceFactory = { spendingsPagingSource },
            initialKey = filterType,
            config = pagingConfig,
        ).flow.cachedIn(viewModelScope)

        isLoading = false
        updateUI()
    }

    private fun updateUI() {
        _spendingsStateFlow.update { spendingsState }
    }
}

data class SpendingsState(
    val spendingsPager: Flow<PagingData<DatedList>>,
    val isPlannedListShown: Boolean,
    val filterType: FilterType,
    val isLoading: Boolean,
    val onPlannedSwitched: (() -> Unit),
)

val pagingConfig = PagingConfig(
    pageSize = 1,
    prefetchDistance = 1,
    enablePlaceholders = false,
    initialLoadSize = 1,
)
