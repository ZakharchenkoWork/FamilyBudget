package com.faigenbloom.famillyspandings.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.domain.statistics.GetCategorySummariesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsPageViewModel(private val getCategorySummariesUseCase: GetCategorySummariesUseCase) :
    ViewModel() {
    private var categorySummary: List<CategorySummary> = emptyList()
    private var sum: Long = 0L
    private var max: Long = 0L

    private var isPieChartOpened: Boolean = true
    private var onPageChanged: (Boolean) -> Unit = {
        isPieChartOpened = it
        _statisicsStateFlow.update { statisicsState }
    }
    private val statisicsState: StatisicsState
        get() = StatisicsState(
            categorySummary = categorySummary,
            sum = sum,
            max = max,
            isPieChartOpened = isPieChartOpened,
            onPageChanged = onPageChanged,
        )

    private val _statisicsStateFlow = MutableStateFlow(statisicsState)
    val statisicsStateFlow = _statisicsStateFlow.asStateFlow().apply {
        viewModelScope.launch {
            categorySummary = getCategorySummariesUseCase()
            sum = categorySummary.sumOf { it.amount }
            max = categorySummary.maxOf { it.amount }
            categorySummary.forEach {
                it.amountPercent = (it.amount.toDouble() / sum.toDouble()) * 100.0
            }
            categorySummary = categorySummary.filter {
                it.amountPercent > 0
            }
            _statisicsStateFlow.update { statisicsState }
        }
    }
}

data class StatisicsState(
    val categorySummary: List<CategorySummary>,
    val sum: Long,
    val max: Long,
    val isPieChartOpened: Boolean,
    val onPageChanged: (Boolean) -> Unit,
)
