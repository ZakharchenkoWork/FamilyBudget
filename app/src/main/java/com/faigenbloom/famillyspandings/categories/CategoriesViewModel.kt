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

    private val onCategoryPhotoUriChanged: (String, Uri) -> Unit = { id, uri ->
        viewModelScope.launch(Dispatchers.IO) {
            categoriesRepository.updateCategoryPhoto(id, uri.toString())
            categoriesList = categoriesRepository.getCategories()
            _categoriesStateFlow.update { categoriesState }
        }
    }

    private val onSelectionChanged: (Int) -> Unit = {
        selectedIndex = it
        _categoriesStateFlow.update { categoriesState }
    }
    private val onNewCategoryNameChanged: (String) -> Unit = {
        newCategoryName = it
        isSaveCategoryVisible = newCategoryName.isNotEmpty()
        _categoriesStateFlow.update { categoriesState }
    }
    private val onNewCategorySaved: () -> Unit = {
        if (newCategoryName.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                categoriesRepository.addCategory(newCategoryName)
                newCategoryName = ""
                categoriesList = categoriesRepository.getCategories()
                _categoriesStateFlow.update { categoriesState }
            }
        }
    }
    private val categoriesState: CategoriesState
        get() = CategoriesState(
            categoriesList = categoriesList,
            selectedIndex = selectedIndex,
            onSelectionChanged = onSelectionChanged,
            newCategoryName = newCategoryName,
            onNewCategoryNameChanged = onNewCategoryNameChanged,
            isSaveCategoryVisible = isSaveCategoryVisible,
            onNewCategorySaved = onNewCategorySaved,
            onCategoryPhotoUriChanged = onCategoryPhotoUriChanged,
            categoryPhotoChooserId = categoryPhotoChooserId,
        )

    private val _categoriesStateFlow = MutableStateFlow(categoriesState)
    val categoriesStateFlow = _categoriesStateFlow.asStateFlow().apply {
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
    val onSelectionChanged: (Int) -> Unit,
    val newCategoryName: String,
    val onNewCategoryNameChanged: (String) -> Unit,
    val isSaveCategoryVisible: Boolean,
    val onNewCategorySaved: () -> Unit,
    val onCategoryPhotoUriChanged: (id: String, photoUri: Uri) -> Unit,
    val categoryPhotoChooserId: String?,
)
