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

    var onCategorySelected: (CategoryUiData) -> Unit = {}
    fun onCategoryIdLoaded(categoryId: String) {
        _stateFlow.update {
            state.copy(
                categoryId = categoryId,
                selectedIndex = if (state.categoriesList.isNotEmpty()) {
                    state.categoriesList.indexOfFirst { it.id == categoryId }
                } else NO_INDEX,
            )
        }
        onCategorySelected(state.categoriesList[state.selectedIndex])
    }

    private fun onCategoryPhotoUriChanged(uri: Uri) {
        _stateFlow.update {
            state.copy(
                newCategoryPhoto = uri,
                isSaveCategoryVisible = true,
            )
        }
    }

    private fun onCategoryError(isError: Boolean) {
        _stateFlow.update {
            state.copy(
                isCategoryError = isError,
            )
        }
    }

    private fun onCategoryDialogVisibilityChanged(categoryIndex: Int) {
        if (state.isEditCategoryShown.not()) {
            if (categoryIndex > NO_INDEX) {
                val categoryUiData = state.categoriesList[categoryIndex]
                _stateFlow.update {
                    state.copy(
                        categoryId = categoryUiData.id,
                        newCategoryName = categoryUiData.name ?: "",
                        newCategoryPhoto = categoryUiData.icon?.toUri(),
                        isEditCategoryShown = true,
                    )
                }
            } else {
                _stateFlow.update {
                    state.copy(
                        categoryId = null,
                        newCategoryName = "",
                        newCategoryPhoto = null,
                        isEditCategoryShown = true,
                    )
                }
            }
        } else {
            _stateFlow.update {
                state.copy(
                    categoryId = null,
                    newCategoryName = "",
                    newCategoryPhoto = null,
                    isSaveCategoryVisible = false,
                    isEditCategoryShown = false,
                )
            }
        }
    }

    private fun onSelectionChanged(selectedIndex: Int) {
        _stateFlow.update {
            state.copy(
                selectedIndex = selectedIndex,
            )
        }
        onCategorySelected(state.categoriesList[selectedIndex])
    }

    private fun onNewCategoryNameChanged(name: String) {
        _stateFlow.update {
            state.copy(
                newCategoryName = name,
                isSaveCategoryVisible = name.isNotBlank(),
            )
        }

    }

    private fun onDeleteCategory() {
        state.categoryId?.let { id ->
            viewModelScope.launch {
                deleteCategoryUseCase(id)
                onCategoryDialogVisibilityChanged(NO_INDEX)
                _stateFlow.update {
                    state.copy(
                        selectedIndex = NO_INDEX,
                        categoriesList = getCategoriesUseCase(false),
                    )
                }
            }
        }
    }

    private fun onNewCategorySaved() {
        if (state.newCategoryName.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val categoryId = setCategoryUseCase(
                    id = state.categoryId ?: "",
                    name = state.newCategoryName,
                    uri = state.newCategoryPhoto,
                )

                onCategoryDialogVisibilityChanged(NO_INDEX)
                val categoriesList = getCategoriesUseCase(false)
                _stateFlow.update {
                    state.copy(
                        categoriesList = categoriesList,
                        selectedIndex = categoriesList.indexOfFirst { it.id == categoryId },
                    )
                }
            }
        }
    }

    private val state: CategoriesState
        get() = _stateFlow.value

    private val _stateFlow = MutableStateFlow(
        CategoriesState(
            onCategoryError = ::onCategoryError,
            onSelectionChanged = ::onSelectionChanged,
            onNewCategoryNameChanged = ::onNewCategoryNameChanged,
            onNewCategorySaved = ::onNewCategorySaved,
            onCategoryPhotoUriChanged = ::onCategoryPhotoUriChanged,
            onCategoryDialogVisibilityChanged = ::onCategoryDialogVisibilityChanged,
            onDeleteCategory = ::onDeleteCategory,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            _stateFlow.update {
                state.copy(
                    categoriesList = getCategoriesUseCase(false),
                )
            }
            state.categoryId?.let {
                onCategoryIdLoaded(it)
            }
        }
    }
}

data class CategoriesState(
    val categoriesList: List<CategoryUiData> = emptyList(),
    val selectedIndex: Int = NO_INDEX,
    val categoryId: String? = null,
    val newCategoryName: String = "",
    val newCategoryPhoto: Uri? = null,
    val isCategoryError: Boolean = false,
    val isSaveCategoryVisible: Boolean = false,
    val isEditCategoryShown: Boolean = false,
    val categoryPhotoChooserId: String? = null,
    val onSelectionChanged: (Int) -> Unit,
    val onNewCategoryNameChanged: (String) -> Unit,
    val onNewCategorySaved: () -> Unit,
    val onCategoryError: (Boolean) -> Unit,
    val onDeleteCategory: () -> Unit,
    val onCategoryDialogVisibilityChanged: (Int) -> Unit,
    val onCategoryPhotoUriChanged: (photoUri: Uri) -> Unit,
)
