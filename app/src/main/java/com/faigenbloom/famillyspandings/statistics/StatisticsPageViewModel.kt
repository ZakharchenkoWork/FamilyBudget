package com.faigenbloom.famillyspandings.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsPageViewModel(private val repository: StatisticsPageRepository) : ViewModel() {
    private val statisicsState: StatisicsState
        get() = StatisicsState(emptyList(), 0L)

    private val _statisicsStateFlow = MutableStateFlow(statisicsState)
    val statisicsStateFlow = _statisicsStateFlow.asStateFlow().apply {
        viewModelScope.launch {
            val categoriesSumaries = repository.getCategoriesSumaries()

            val sum = categoriesSumaries.sumOf { it.amount }
            categoriesSumaries.forEach {
                it.amountPercent = (it.amount.toDouble() / sum.toDouble()) * 100.0
            }
            _statisicsStateFlow.update {
                StatisicsState(
                    categorySummary = categoriesSumaries.filter {
                        it.amountPercent > 0
                    },
                    sum = sum,
                )
            }
        }
    }
}

data class StatisicsState(
    val categorySummary: List<CategorySummary>,
    val sum: Long,
)
