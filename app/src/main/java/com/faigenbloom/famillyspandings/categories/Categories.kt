@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.BaseTextField
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun Categories(
    state: CategoriesState,
    onCategoryPhotoRequest: (id: String) -> Unit,
) {
    LazyColumn(
        content = {
            item {
                AddNewCategory(state)
            }
            items(state.categoriesList.size) { itemIndex ->
                Category(state, itemIndex, onCategoryPhotoRequest)
            }
        },
    )
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
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(64.dp)
                .clickable {
                    onCategoryPhotoRequest(item.id)
                },
            contentScale = ContentScale.Crop,
            painter =
            item.iconUri?.let {
                rememberImagePainter(it)
            } ?: item.iconId?.let {
                painterResource(id = it)
            } ?: painterResource(id = R.drawable.photo),
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
            Image(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                painter = painterResource(id = R.drawable.icon_ok),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun AddNewCategory(state: CategoriesState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(color = MaterialTheme.colorScheme.tertiary),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(64.dp),
            painter = painterResource(id = R.drawable.plus),
            contentDescription = null,
        )

        BaseTextField(
            modifier = Modifier
                .weight(0.8f)
                .padding(horizontal = 8.dp),
            text = state.newCategoryName,
            labelId = R.string.category_new,
            onTextChange = { state.onNewCategoryNameChanged(it) },
        )

        Image(
            modifier = Modifier
                .clickable {
                    if (state.isSaveCategoryVisible) {
                        state.onNewCategorySaved()
                    }
                }
                .weight(0.2f)
                .alpha(
                    if (state.isSaveCategoryVisible) {
                        1.0f
                    } else {
                        0.0f
                    },
                )
                .padding(horizontal = 8.dp),
            painter = painterResource(id = R.drawable.icon_ok),
            contentDescription = null,
        )
    }
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun SpendingEditPagePreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            Categories(
                state = CategoriesState(
                    categoriesList = mockCategoriesList,
                    selectedIndex = 1,
                    onSelectionChanged = {},
                    newCategoryName = "",
                    onNewCategoryNameChanged = {},
                    isSaveCategoryVisible = false,
                    onNewCategorySaved = { },
                    onCategoryPhotoUriChanged = { _, _ -> },
                    categoryPhotoChooserId = null,
                ),
                onCategoryPhotoRequest = {},
            )
        }
    }
}
