@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.ui.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberImagePainter
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.common.BaseTextField
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun CategoriesPage(
    state: CategoriesState,
    onCategoryPhotoRequest: (id: String?) -> Unit,
) {

    Box {
        LazyColumn(
            content = {
                items(state.categoriesList.size) { itemIndex ->
                    Category(state, itemIndex, onCategoryPhotoRequest)
                }
            },
        )
        AddNewCategory(state, onCategoryPhotoRequest)
    }
}

@Composable
fun Category(
    state: CategoriesState,
    itemIndex: Int,
    onCategoryPhotoRequest: (id: String) -> Unit,
) {
    val item = state.categoriesList[itemIndex]
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = if (state.selectedIndex == itemIndex) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.tertiary
                },
            )
            .clickable {
                state.onSelectionChanged(itemIndex)
            }
            .semantics {
                contentDescription = if (itemIndex == 0) FIRST_CATEGORY else ""
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(64.dp)
                .aspectRatio(1f)
                .clickable {
                    onCategoryPhotoRequest(item.id)
                },
            contentScale = ContentScale.Crop,
            painter =
            item.iconUri?.let {
                rememberImagePainter(it)
            } ?: item.iconId?.let {
                painterResource(id = it)
            } ?: painterResource(id = R.drawable.icon_photo),
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f),
            text = item.nameId?.let {
                stringResource(id = it)
            } ?: item.name ?: "",
            color = MaterialTheme.colorScheme.secondary,
        )

        if (state.selectedIndex == itemIndex) {
            if (item.isDefault.not()) {
                Image(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            state.onCategoryDialogVisibilityChanged(itemIndex)
                        }
                        .semantics {
                            contentDescription = EDIT_CATEGORY_BUTTON
                        },
                    painter = painterResource(id = R.drawable.icon_edit),
                    contentDescription = null,
                )
            }
            Image(
                modifier = Modifier
                    .padding(end = 16.dp),
                painter = painterResource(id = R.drawable.icon_ok),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun AddNewCategory(
    state: CategoriesState,
    onCategoryPhotoRequest: (id: String?) -> Unit,
) {
    if (state.isEditCategoryShown) {
        Dialog(
            onDismissRequest = { state.onCategoryDialogVisibilityChanged(NO_INDEX) },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background),
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .weight(0.5f),
                        text = stringResource(
                            id = state.categoryId?.let {
                                R.string.category_edit
                            } ?: R.string.category_new,
                        ),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    state.categoryId?.let {
                        Box(
                            modifier = Modifier
                                .weight(0.5f),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(16.dp)
                                    .aspectRatio(1f)
                                    .clickable {
                                        state.onDeleteCategory()
                                    },
                                painter = painterResource(id = R.drawable.icon_delete),
                                contentDescription = null,
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .clickable {
                                onCategoryPhotoRequest(state.categoryPhotoChooserId)
                            }
                            .size(64.dp)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop,
                        painter = state.newCategoryPhoto?.let {
                            rememberImagePainter(it)
                        } ?: painterResource(id = R.drawable.icon_photo),
                        contentDescription = null,
                    )
                    val focusManager = LocalFocusManager.current
                    BaseTextField(
                        modifier = Modifier
                            .weight(0.8f)
                            .padding(horizontal = 8.dp)
                            .semantics {
                                contentDescription = NEW_CATEGORY_NAME_INPUT
                            },
                        text = state.newCategoryName,
                        labelId = R.string.category_name_hint,
                        onTextChange = { state.onNewCategoryNameChanged(it) },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                state.onNewCategorySaved()
                            },
                        ),
                    )
                    if (state.isSaveCategoryVisible) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(0.2f)
                                .clickable {
                                    focusManager.clearFocus()
                                    state.onNewCategorySaved()
                                }
                                .semantics {
                                    contentDescription = NEW_CATEGORY_SAVE_BUTTON
                                },
                            painter = painterResource(id = R.drawable.icon_ok),
                            contentDescription = "",
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(0.2f),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddNewCategoryPreview() {
    FamillySpandingsTheme {
        Surface {
            AddNewCategory(
                state = CategoriesState(
                    categoriesList = mockCategoriesList,
                    selectedIndex = 1,
                    onSelectionChanged = {},
                    categoryId = "null",
                    newCategoryName = "Any",
                    onNewCategoryNameChanged = {},
                    isSaveCategoryVisible = true,
                    onNewCategorySaved = { },
                    onCategoryPhotoUriChanged = { },
                    categoryPhotoChooserId = null,
                    newCategoryPhoto = null,
                    isEditCategoryShown = true,
                    onCategoryDialogVisibilityChanged = { },

                    onDeleteCategory = { },
                ),
                onCategoryPhotoRequest = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpendingEditPagePreview() {
    FamillySpandingsTheme {
        Surface {
            CategoriesPage(
                state = CategoriesState(
                    categoriesList = mockCategoriesList,
                    selectedIndex = 1,
                    onSelectionChanged = {},
                    newCategoryName = "",
                    onNewCategoryNameChanged = {},
                    isSaveCategoryVisible = false,
                    onNewCategorySaved = { },
                    onCategoryPhotoUriChanged = { },
                    categoryPhotoChooserId = null,
                    newCategoryPhoto = null,
                    isEditCategoryShown = false,
                    onCategoryDialogVisibilityChanged = { },
                    categoryId = null,
                    onDeleteCategory = { },
                ),
                onCategoryPhotoRequest = {},
            )
        }
    }
}
