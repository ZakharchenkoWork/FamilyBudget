package com.faigenbloom.familybudget.ui.categories

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.domain.categories.DeleteCategoryUseCase
import com.faigenbloom.familybudget.domain.categories.GetCategoriesUseCase
import com.faigenbloom.familybudget.domain.categories.SetCategoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val NO_INDEX = -1

class CategoriesViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase<CategoryUiData>,
    private val setCategoryUseCase: SetCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
) : ViewModel() {

    private var categoriesList: List<CategoryUiData> = emptyList()
    private var selectedIndex = NO_INDEX
    private var categoryId: String? = null
    private var newCategoryName: String = ""
    private var newCategoryPhoto: Uri? = null
    private var isCategoryError: Boolean = false
    private var categoryPhotoChooserId: String? = null
    private var isSaveCategoryVisible: Boolean = false
    private var isEditCategoryShown: Boolean = false

    var onCategorySelected: (CategoryUiData) -> Unit = {}

    private fun onCategoryPhotoUriChanged(uri: Uri) {
        newCategoryPhoto = uri
        isSaveCategoryVisible = true
        updateUI()
    }

    private fun onCategoryError(isError: Boolean) {
        isCategoryError = isError
        updateUI()
    }

    private fun onCategoryDialogVisibilityChanged(categoryIndex: Int) {
        isEditCategoryShown = !isEditCategoryShown
        if (isEditCategoryShown) {
            if (categoryIndex >= 0) {
                val categoryUiData = categoriesList[categoryIndex]
                categoryId = categoryUiData.id
                newCategoryName = categoryUiData.name ?: ""
                newCategoryPhoto = categoryUiData.iconUri?.toUri()
            } else {
                categoryId = null
                newCategoryName = ""
                newCategoryPhoto = null
            }
        } else {
            categoryId = null
            newCategoryName = ""
            newCategoryPhoto = null
            isSaveCategoryVisible = false
            isEditCategoryShown = false
        }
        updateUI()
    }

    private fun onSelectionChanged(selectedIndex: Int) {
        this.selectedIndex = selectedIndex
        onCategorySelected(categoriesList[selectedIndex])
        updateUI()
    }

    private fun onNewCategoryNameChanged(name: String) {
        this.newCategoryName = name
        isSaveCategoryVisible = newCategoryName.isNotBlank()
        updateUI()
    }

    private fun onDeleteCategory() {
        categoryId?.let { id ->
            viewModelScope.launch {
                deleteCategoryUseCase(id)
                onCategoryDialogVisibilityChanged(NO_INDEX)
                selectedIndex = NO_INDEX
                categoriesList = getCategoriesUseCase(false)
                updateUI()
            }
        }
    }

    private fun onNewCategorySaved() {
        if (newCategoryName.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val categoryId = setCategoryUseCase(
                    id = categoryId ?: "",
                    name = newCategoryName,
                    uri = newCategoryPhoto,
                )

                onCategoryDialogVisibilityChanged(NO_INDEX)
                categoriesList = getCategoriesUseCase(false)
                selectedIndex = categoriesList.indexOfFirst { it.id == categoryId }
                updateUI()
            }
        }
    }

    private val state: CategoriesState
        get() = CategoriesState(
            categoriesList = categoriesList,
            categoryId = categoryId,
            selectedIndex = selectedIndex,
            newCategoryName = newCategoryName,
            newCategoryPhoto = newCategoryPhoto,
            isSaveCategoryVisible = isSaveCategoryVisible,
            categoryPhotoChooserId = categoryPhotoChooserId,
            isEditCategoryShown = isEditCategoryShown,
            isCategoryError = isCategoryError,
            onCategoryError = ::onCategoryError,
            onSelectionChanged = ::onSelectionChanged,
            onNewCategoryNameChanged = ::onNewCategoryNameChanged,
            onNewCategorySaved = ::onNewCategorySaved,
            onCategoryPhotoUriChanged = ::onCategoryPhotoUriChanged,
            onCategoryDialogVisibilityChanged = ::onCategoryDialogVisibilityChanged,
            onDeleteCategory = ::onDeleteCategory,
        )

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesList = getCategoriesUseCase(false)
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
    val categoryId: String?,
    val newCategoryName: String,
    val newCategoryPhoto: Uri?,
    val onNewCategoryNameChanged: (String) -> Unit,
    val isSaveCategoryVisible: Boolean,
    val isEditCategoryShown: Boolean,
    val isCategoryError: Boolean,
    val onNewCategorySaved: () -> Unit,
    val onCategoryError: (Boolean) -> Unit,
    val onDeleteCategory: () -> Unit,
    val onCategoryDialogVisibilityChanged: (Int) -> Unit,
    val onCategoryPhotoUriChanged: (photoUri: Uri) -> Unit,
    val categoryPhotoChooserId: String?,
)
