package com.faigenbloom.famillyspandings.categories

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class CategoriesViewModel(private val categoriesRepository: CategoriesRepository) :
    ViewModel() {
    private var categoriesList: List<CategoryData> = emptyList()
    private var selectedIndex = -1
    private var newCategoryName: String = ""
    private var isSaveCategoryVisible: Boolean = false
    private var categoryPhotoChooserId: String? = null
    protected var isCategorySelected: Boolean = false

    private fun onCategoryPhotoUriChanged(id: String, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesRepository.updateCategoryPhoto(id, uri.toString())
            categoriesList = categoriesRepository.getCategories()
            _categoriesStateFlow.update { categoriesState }
        }
    }

    private fun onSelectionChanged(selectedIndex: Int) {
        this.selectedIndex = selectedIndex
        isCategorySelected = true
        _categoriesStateFlow.update { categoriesState }
    }

    private fun onNewCategoryNameChanged(name: String) {
        this.newCategoryName = name
        isSaveCategoryVisible = newCategoryName.isNotEmpty()
        _categoriesStateFlow.update { categoriesState }
    }

    private fun onNewCategorySaved() {
        if (newCategoryName.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val categoryId = categoriesRepository.addCategory(newCategoryName)
                newCategoryName = ""
                categoriesList = categoriesRepository.getCategories()
                selectedIndex = categoriesList.indexOfFirst { it.id == categoryId }
                isCategorySelected = true
                _categoriesStateFlow.update { categoriesState }
            }
        }
    }

    private val categoriesState: CategoriesState
        get() = CategoriesState(
            categoriesList = categoriesList,
            selectedIndex = selectedIndex,
            newCategoryName = newCategoryName,
            isSaveCategoryVisible = isSaveCategoryVisible,
            categoryPhotoChooserId = categoryPhotoChooserId,
            onSelectionChanged = ::onSelectionChanged,
            onNewCategoryNameChanged = ::onNewCategoryNameChanged,
            onNewCategorySaved = ::onNewCategorySaved,
            onCategoryPhotoUriChanged = ::onCategoryPhotoUriChanged,
        )

    private val _categoriesStateFlow = MutableStateFlow(categoriesState)
    val categoriesStateFlow = _categoriesStateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesList = categoriesRepository.getCategories()
            _categoriesStateFlow.update { categoriesState }
        }
    }

    protected fun getSelectedCategory() = categoriesList[selectedIndex]
    protected fun setSelectedCategory(categoryId: String) {
        selectedIndex = categoriesList.indexOfFirst { it.id == categoryId }
        isCategorySelected = true
        _categoriesStateFlow.update { categoriesState }
    }
}

data class CategoriesState(
    val categoriesList: List<CategoryData>,
    val selectedIndex: Int,
    val onSelectionChanged: (Int) -> Unit,
    val newCategoryName: String,
    val onNewCategoryNameChanged: (String) -> Unit,
    val isSaveCategoryVisible: Boolean,
    val onNewCategorySaved: () -> Unit,
    val onCategoryPhotoUriChanged: (id: String, photoUri: Uri) -> Unit,
    val categoryPhotoChooserId: String?,
)
