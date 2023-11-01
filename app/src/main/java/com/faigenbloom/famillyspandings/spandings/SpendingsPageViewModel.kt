package com.faigenbloom.famillyspandings.spandings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.comon.Pattern
import com.faigenbloom.famillyspandings.comon.PlatesSorter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SpendingsPageViewModel(private val repository: SpendingsPageRepository) : ViewModel() {
    private val sorter = PlatesSorter<SpendingData>()
    private var spendings: List<List<Pattern<SpendingData>>> = emptyList()

    init {
        viewModelScope.launch {
            spendings = sorter.prepareByDates(repository.getAllSpendings()).map {
                sorter.findPattern(
                    sorter.preparePlatesSizes(it),
                )
            }
        }
    }

    private val spendingsState: SpendingsState
        get() = SpendingsState(spendings)

    private val _spendingsStateFlow = MutableStateFlow(spendingsState)
    val spendingsStateFlow = _spendingsStateFlow.asStateFlow()
}

data class SpendingsState(
    val spendings: List<List<Pattern<SpendingData>>>,
)
