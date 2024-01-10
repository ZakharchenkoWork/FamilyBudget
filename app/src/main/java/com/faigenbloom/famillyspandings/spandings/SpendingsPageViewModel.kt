package com.faigenbloom.famillyspandings.spandings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.Pattern
import com.faigenbloom.famillyspandings.comon.PlatesSorter
import com.faigenbloom.famillyspandings.comon.toLocalDate
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpendingsPageViewModel(
    private val repository: SpendingsPageRepository,
) : ViewModel() {
    private var lastSpendings: List<SpendingData> = emptyList()
    private val sorter = PlatesSorter<SpendingData>()
    private var spendings: List<List<Pattern<SpendingData>>> = emptyList()
    private var isPlanned: Boolean = false
    private var isLoading: Boolean = true
    private val onPlannedSwitched: (() -> Unit) = {
        isPlanned = isPlanned.not()
        reloadData()
    }
    private val spendingsState: SpendingsState
        get() = SpendingsState(
            spendings,
            isPlannedListShown = isPlanned,
            isLoading = isLoading,
            onPlannedSwitched,
        )

    private val _spendingsStateFlow = MutableStateFlow(spendingsState)
    val spendingsStateFlow = _spendingsStateFlow.asStateFlow().apply {
        reloadData()
    }

    fun reloadData() {
        isLoading = true
        updateUI()
        viewModelScope.launch(Dispatchers.IO) {
            val spendingsUpdatedList = repository.getSpendings(isPlanned).map {
                it.toSpendingData(repository.getCategoryById(it.categoryId))
            }
            if (lastSpendings != spendingsUpdatedList) {
                lastSpendings = spendingsUpdatedList
                spendings = sorter.prepareByDates(
                    lastSpendings,
                ).sortedByDescending {
                    it[0].getSortableDate()
                }.map {
                    sorter.findPattern(
                        sorter.preparePlatesSizes(it),
                    )
                }
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
    val spendings: List<List<Pattern<SpendingData>>>,
    val isPlannedListShown: Boolean,
    val isLoading: Boolean,
    val onPlannedSwitched: (() -> Unit),
)

fun SpendingEntity.toSpendingData(category: CategoryEntity): SpendingData {
    return SpendingData(
        id = id,
        name = name,
        category = CategoryData.fromEntity(category),
        amount = amount,
        date = date.toLocalDate(),
        isHidden = isHidden,
    )
}
