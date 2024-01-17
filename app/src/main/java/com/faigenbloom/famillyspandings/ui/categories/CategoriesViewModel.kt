package com.faigenbloom.famillyspandings.ui.categories

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.domain.categories.AddCategoryUseCase
import com.faigenbloom.famillyspandings.domain.categories.GetCategoriesUseCase
import com.faigenbloom.famillyspandings.domain.categories.UpdateCategoryPhotoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class CategoriesViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase<CategoryUiData>,
    private val updateCategoryPhotoUseCase: UpdateCategoryPhotoUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
) : ViewModel() {

    private var categoriesList: List<CategoryUiData> = emptyList()
    private var selectedIndex = -1
    private var categoryId: String? = null
    private var newCategoryName: String = ""
    private var isSaveCategoryVisible: Boolean = false
    private var categoryPhotoChooserId: String? = null

    var onCategorySelected: (CategoryUiData) -> Unit = {}

    private fun onCategoryPhotoUriChanged(id: String, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            updateCategoryPhotoUseCase(id, uri.toString())
            categoriesList = getCategoriesUseCase()
            updateUI()
        }
    }

    private fun onSelectionChanged(selectedIndex: Int) {
        this.selectedIndex = selectedIndex
        onCategorySelected(categoriesList[selectedIndex])
        updateUI()
    }

    private fun onNewCategoryNameChanged(name: String) {
        this.newCategoryName = name
        isSaveCategoryVisible = newCategoryName.isNotEmpty()
        updateUI()
    }

    private fun onNewCategorySaved() {
        if (newCategoryName.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val categoryId = addCategoryUseCase(newCategoryName)
                newCategoryName = ""
                categoriesList = getCategoriesUseCase()
                selectedIndex = categoriesList.indexOfFirst { it.id == categoryId }
                isSaveCategoryVisible = false
                updateUI()
            }
        }
    }

    private val state: CategoriesState
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

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesList = getCategoriesUseCase()
            categoryId?.let {
                selectedIndex = categoriesList.indexOfFirst { it.id == categoryId }
                onCategorySelected(categoriesList[selectedIndex])
            }
            updateUI()
        }
    }

    fun onCategoryIdLoaded(it: String) {
        categoryId = it
        if (categoriesList.isNotEmpty()) {
            selectedIndex = categoriesList.indexOfFirst { it.id == categoryId }
            onCategorySelected(categoriesList[selectedIndex])
            updateUI()
        }
    }

    private fun updateUI() {
        _stateFlow.update { state }
    }
}

data class CategoriesState(
    val categoriesList: List<CategoryUiData>,
    val selectedIndex: Int,
    val onSelectionChanged: (Int) -> Unit,
    val newCategoryName: String,
    val onNewCategoryNameChanged: (String) -> Unit,
    val isSaveCategoryVisible: Boolean,
    val onNewCategorySaved: () -> Unit,
    val onCategoryPhotoUriChanged: (id: String, photoUri: Uri) -> Unit,
    val categoryPhotoChooserId: String?,
)
