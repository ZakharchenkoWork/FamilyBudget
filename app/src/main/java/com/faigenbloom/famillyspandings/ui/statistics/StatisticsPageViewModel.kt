package com.faigenbloom.famillyspandings.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import com.faigenbloom.famillyspandings.domain.GetChosenCurrencyUseCase
import com.faigenbloom.famillyspandings.domain.statistics.GetCategorySummariesUseCase
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

    private var isPieChartOpened: Boolean = true
    private var onPageChanged: (Boolean) -> Unit = {
        isPieChartOpened = it
        updateUi()
    }
    private val state: StatisicsState
        get() = StatisicsState(
            categorySummary = summaries,
            sum = sum,
            max = max,
            currency = currency,
            sideLabelValues = sideLabelValues,
            isPieChartOpened = isPieChartOpened,
            onPageChanged = onPageChanged,
        )

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow().apply {
        viewModelScope.launch {
            summaries = getCategorySummariesUseCase()
            sum = summaries.sumOf { it.amount }.toReadableMoney()
            max = summaries.maxOf { it.amount }
            sideLabelValues = Array<String>(10) {
                "${(max / 10) * it}"
            }

            currency = getChosenCurrencyUseCase()
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
    val isPieChartOpened: Boolean,
    val onPageChanged: (Boolean) -> Unit,
)
