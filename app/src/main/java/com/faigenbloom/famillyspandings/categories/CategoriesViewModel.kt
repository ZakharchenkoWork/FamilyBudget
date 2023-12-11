package com.faigenbloom.famillyspandings.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class CategoriesViewModel(private val categoriesRepository: CategoriesRepository) :
    ViewModel() {
    private var selectedIndex = -1
    private var onSelectionChanged: (Int) -> Unit = {
        selectedIndex = it
        _categoriesStateFlow.update { categoriesState }
    }
    private var categoriesList: List<CategoryData> = emptyList()
    private val categoriesState: CategoriesState
        get() = CategoriesState(
            categoriesList = categoriesList,
            selectedIndex = selectedIndex,
            onSelectionChanged = onSelectionChanged,
        )

    private val _categoriesStateFlow = MutableStateFlow(categoriesState)
    val categoriesStateFlow = _categoriesStateFlow.asStateFlow()
        .apply {
            viewModelScope.launch(Dispatchers.IO) {
                categoriesList = categoriesRepository.getCategories()
                _categoriesStateFlow.update { categoriesState }
            }
        }

    protected fun getSelectedCategory() = categoriesList[selectedIndex]
}

data class CategoriesState(
    val categoriesList: List<CategoryData>,
    val selectedIndex: Int,
    var onSelectionChanged: (Int) -> Unit = {},
)
