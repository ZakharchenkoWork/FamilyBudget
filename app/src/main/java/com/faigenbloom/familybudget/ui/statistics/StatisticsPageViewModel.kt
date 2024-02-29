package com.faigenbloom.familybudget.ui.statistics

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.common.getCurrentDate
import com.faigenbloom.familybudget.common.toLongDate
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.domain.currency.GetChosenCurrencyUseCase
import com.faigenbloom.familybudget.domain.statistics.FilterType
import com.faigenbloom.familybudget.domain.statistics.GetCategorySummariesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

class StatisticsPageViewModel(
    private val getCategorySummariesUseCase: GetCategorySummariesUseCase,
    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,
) : ViewModel() {
    var onCalendarRequested: (fromDate: String, toDate: String) -> Unit = { _, _ -> }

    fun onDateRangeChanged(fromDate: String, toDate: String) {
        if (fromDate.isNotBlank()) {
            val filterType =
                FilterType.Range(fromDate.toLongDate(), toDate.ifBlank { fromDate }.toLongDate())
            reloadData(filterType)
        }
    }

    private fun onPageChanged(isLeft: Boolean) {
        _stateFlow.update { state ->
            state.copy(
                isPieChartOpened = isLeft,
            )
        }
    }

    private fun onRangeClicked() {
        if (state.filterType is FilterType.Yearly) {
            onCalendarRequested(
                getCurrentDate().toReadableDate(),
                getCurrentDate().toReadableDate(),
            )
        } else {
            onCalendarRequested(
                state.filterType.from.toReadableDate(),
                state.filterType.to.toReadableDate(),
            )
        }
    }

    private fun onYearlyClicked() {
        reloadData(FilterType.Yearly())
    }

    private fun onMonthlyClicked() {
        reloadData(FilterType.Monthly())
    }

    private fun onDailyClicked() {
        reloadData(FilterType.Daily())
    }

    private fun onDateMoved(isRight: Boolean) {
        reloadData(state.filterType.move(isRight))
    }

    private val state: StatisicsState
        get() = _stateFlow.value
    private val _stateFlow = MutableStateFlow(
        StatisicsState(
            rangeClicked = ::onRangeClicked,
            onDateMoved = ::onDateMoved,
            yearlyClicked = ::onYearlyClicked,
            monthlyClicked = ::onMonthlyClicked,
            dailyClicked = ::onDailyClicked,
            onPageChanged = ::onPageChanged,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        reloadData()
    }

    private fun reloadData(filterType: FilterType = state.filterType) {
        state.isLoading.value = true
        viewModelScope.launch {
            val summaries = getCategorySummariesUseCase(filterType)
            if (summaries.isEmpty()) {
                _stateFlow.update { state ->
                    state.copy(
                        isNoDataToShow = true,
                    )
                }
            } else {
                val max = summaries.maxOf { it.amount }
                _stateFlow.update { state ->
                    state.copy(
                        categorySummary = summaries,
                        isNoDataToShow = false,
                        sum = summaries.sumOf { it.amount }.toReadableMoney(),
                        max = summaries.maxOf { it.amount },
                        sideLabelValues = Array(10) {
                            "${(max / 10) * it}"
                        },
                        currency = getChosenCurrencyUseCase(),
                    )
                }
            }
            state.isLoading.value = false
        }
    }
}

data class StatisicsState(
    val categorySummary: List<CategorySummaryUi> = emptyList(),
    val sum: String = "",
    val max: Long = 0L,
    val currency: Currency = Currency.getInstance(Locale.getDefault()),
    val sideLabelValues: Array<String> = emptyArray(),
    val filterType: FilterType = FilterType.Monthly(),
    val isPieChartOpened: Boolean = true,
    val isNoDataToShow: Boolean = false,
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val rangeClicked: () -> Unit,
    val yearlyClicked: () -> Unit,
    val monthlyClicked: () -> Unit,
    val dailyClicked: () -> Unit,
    val onDateMoved: (isRight: Boolean) -> Unit,
    val onPageChanged: (Boolean) -> Unit,
)





