package com.faigenbloom.familybudget.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.common.getCurrentDate
import com.faigenbloom.familybudget.common.toLongDate
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.domain.GetChosenCurrencyUseCase
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
    private var summaries: List<CategorySummaryUi> = emptyList()
    private var sum: String = ""
    private var max: Long = 0L
    private var sideLabelValues: Array<String> = emptyArray()
    private var currency = Currency.getInstance(Locale.getDefault())
    private var filterType: FilterType = FilterType.Monthly()
    private var fromDate: Long = filterType.from
    private var toDate: Long = filterType.to
    private var isNoDataToShow: Boolean = false


    private var isPieChartOpened: Boolean = true
    private var onPageChanged: (Boolean) -> Unit = {
        isPieChartOpened = it
        updateUi()
    }

    var onCalendarRequested: (fromDate: String, toDate: String) -> Unit = { _, _ -> }

    val onDateRangeChanged: (fromDate: String, toDate: String) -> Unit = { fromDate, toDate ->
        if (fromDate.isNotBlank()) {
            this.fromDate = fromDate.toLongDate()
            this.toDate = toDate.ifBlank { fromDate }.toLongDate()
            filterType = FilterType.Range(this.fromDate, this.toDate)
            reloadData()
        }
    }

    private fun onRangeClicked() {
        if (filterType is FilterType.Yearly) {
            onCalendarRequested(
                getCurrentDate().toReadableDate(),
                getCurrentDate().toReadableDate(),
            )
        } else {
            onCalendarRequested(
                fromDate.toReadableDate(),
                toDate.toReadableDate(),
            )
        }
    }

    private fun onYearlyClicked() {
        filterType = FilterType.Yearly()
        fromDate = filterType.from
        toDate = filterType.to
        reloadData()
    }

    private fun onMonthlyClicked() {
        filterType = FilterType.Monthly()
        fromDate = filterType.from
        toDate = filterType.to
        reloadData()
    }

    private fun onDailyClicked() {
        filterType = FilterType.Daily()
        fromDate = filterType.from
        toDate = filterType.to
        reloadData()
    }

    private fun onDateMoved(isRight: Boolean) {
        filterType = filterType.move(isRight)
        fromDate = filterType.from
        toDate = filterType.to
        reloadData()
    }

    private val state: StatisicsState
        get() = StatisicsState(
            categorySummary = summaries,
            sum = sum,
            max = max,
            currency = currency,
            sideLabelValues = sideLabelValues,
            isPieChartOpened = isPieChartOpened,
            rangeClicked = ::onRangeClicked,
            filterType = filterType,
            isNoDataToShow = isNoDataToShow,
            onDateMoved = ::onDateMoved,
            yearlyClicked = ::onYearlyClicked,
            monthlyClicked = ::onMonthlyClicked,
            dailyClicked = ::onDailyClicked,
            onPageChanged = onPageChanged,
        )

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow().apply {
        reloadData()
    }

    private fun reloadData() {
        viewModelScope.launch {
            summaries = getCategorySummariesUseCase(filterType)
            if (summaries.isEmpty()) {
                isNoDataToShow = true
                updateUi()
                return@launch
            }
            sum = summaries.sumOf { it.amount }.toReadableMoney()
            max = summaries.maxOf { it.amount }
            sideLabelValues = Array(10) {
                "${(max / 10) * it}"
            }

            currency = getChosenCurrencyUseCase()
            isNoDataToShow = false
            updateUi()
        }
    }

    private fun updateUi() {
        _stateFlow.update { state }
    }
}

data class StatisicsState(
    val categorySummary: List<CategorySummaryUi>,
    val sum: String,
    val max: Long,
    val currency: Currency,
    val sideLabelValues: Array<String>,
    val filterType: FilterType,
    val isPieChartOpened: Boolean,
    val isNoDataToShow: Boolean,
    val rangeClicked: () -> Unit,
    val yearlyClicked: () -> Unit,
    val monthlyClicked: () -> Unit,
    val dailyClicked: () -> Unit,
    val onDateMoved: (isRight: Boolean) -> Unit,
    val onPageChanged: (Boolean) -> Unit,
)





