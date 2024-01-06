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
    private val sorter = PlatesSorter<SpendingData>()
    private var spendings: List<List<Pattern<SpendingData>>> = emptyList()

    private val spendingsState: SpendingsState
        get() = SpendingsState(spendings)

    private val _spendingsStateFlow = MutableStateFlow(spendingsState)
    val spendingsStateFlow = _spendingsStateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            spendings = sorter.prepareByDates(
                repository.getAllSpendings().map {
                    it.toSpendingData(repository.getCategoryById(it.categoryId))
                },
            ).map {
                sorter.findPattern(
                    sorter.preparePlatesSizes(it),
                )
            }
            updateUI()
        }
    }

    fun updateUI() {
        _spendingsStateFlow.update { spendingsState }
    }
}

data class SpendingsState(
    val spendings: List<List<Pattern<SpendingData>>>,
)

fun SpendingEntity.toSpendingData(category: CategoryEntity): SpendingData {
    return SpendingData(
        id = id,
        name = name,
        category = CategoryData.fromEntity(category),
        amount = amount,
        date = date.toLocalDate(),
    )
}
