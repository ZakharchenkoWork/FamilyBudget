package com.faigenbloom.famillyspandings.spandings.edit

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.categories.Categories
import com.faigenbloom.famillyspandings.categories.CategoriesState
import com.faigenbloom.famillyspandings.comon.BaseTextField
import com.faigenbloom.famillyspandings.comon.SimpleTextField
import com.faigenbloom.famillyspandings.comon.StripeBar
import com.faigenbloom.famillyspandings.comon.TextFieldType
import com.faigenbloom.famillyspandings.comon.TopBar
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import com.faigenbloom.famillyspandings.ui.theme.hint

typealias CategoriesMock = com.faigenbloom.famillyspandings.categories.Mock

@Composable
fun SpendingEditPage(
    state: SpendingEditState,
    onPhotoRequest: (id: String) -> Unit,
    onCategoryPhotoRequest: (id: String) -> Unit,
) {
    Column {
        TopBar(title = stringResource(id = R.string.adding_spending_title))
        StripeBar(
            textId = R.string.choose_category,
            secondTabTextId = R.string.spending_information,
            isLeftSelected = state.isCategoriesOpened,
            onSelectionChanged = state.onPageChanged,
        )
        if (state.isCategoriesOpened) {
            Categories(
                state = state.categoryState,
                onCategoryPhotoRequest = onCategoryPhotoRequest,
            )
        } else {
            Information(
                state = state,
                onPhotoRequest = onPhotoRequest,
            )
        }
    }
}

@Composable
fun Information(state: SpendingEditState, onPhotoRequest: (id: String) -> Unit) {
    Box {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .wrapContentSize()
                        .clickable {
                            onPhotoRequest(state.spendingId)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier.size(170.dp),
                        painter = state.photoUri?.let {
                            rememberImagePainter(it) {
                                transformations(CircleCropTransformation())
                            }
                        } ?: painterResource(id = R.drawable.photo),
                        contentDescription = "",
                    )
                }
                Column(
                    modifier = Modifier.weight(0.5f),
                    verticalArrangement = Arrangement.Center,
                ) {
                    BaseTextField(
                        text = state.namingText,
                        labelId = R.string.spending_details_naming,
                        onTextChange = state.onNamingTextChanged,
                    )
                    BaseTextField(
                        text = state.amountText,
                        labelId = R.string.spending_details_amount,
                        textFieldType = TextFieldType.Money,
                        onTextChange = state.onAmountTextChanged,
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                            .clickable {
                                state.onCalendarVisibilityChanged(true)
                            },
                        text = state.dateText,
                    )
                }
            }
            SpacerStripe(
                modifier = Modifier.padding(top = 16.dp),
                textId = R.string.spending_details,
            )
            DatailsList(state)
        }
        if (state.isCalendarOpen) {
            // Calendar()
        }
    }
}

@Composable
fun DatailsList(state: SpendingEditState) {
    LazyColumn {
        items(state.detailsList.size + 1) { detailIndex ->
            if (detailIndex < state.detailsList.size) {
                DetailsItem(
                    state = state,
                    detailIndex = detailIndex,
                )
            } else {
                AddItemLine(state = state)
            }
        }
    }
}

@Composable
fun AddItemLine(state: SpendingEditState) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    state.onAddNewDetail()
                },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
            )
            Text(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 8.dp),
                text = stringResource(R.string.spending_add_detail_hint),
                color = MaterialTheme.colorScheme.hint(),
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.secondary),
        )
    }
}

@Composable
fun DetailsItem(
    state: SpendingEditState,
    detailIndex: Int,
) {
    val spendingDetail = state.detailsList[detailIndex]
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SimpleTextField(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 8.dp),
                text = spendingDetail.name,
                label = stringResource(R.string.spending_name),
                onValueChange = { state.onDetailNameChanged(detailIndex, it) },
            )
            SimpleTextField(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.End,
                text = spendingDetail.amount,
                textFieldType = TextFieldType.Money,
                label = stringResource(R.string.spending_details_amount),
                onValueChange = { state.onDetailAmountChanged(detailIndex, it) },
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.secondary),
        )
    }
}

@Composable
fun SpacerStripe(
    modifier: Modifier = Modifier,
    @StringRes textId: Int,
    secondTabTextId: Int? = null,
    isLeftSelected: Boolean = true,
    onSelectionChanged: ((Boolean) -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .weight(0.5f)
                .clickable { onSelectionChanged?.invoke(true) }
                .background(
                    color = if (isLeftSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    },
                ),
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = stringResource(id = textId),
                style = MaterialTheme.typography.titleMedium,
                color = if (isLeftSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onPrimary
                },
            )
        }
        secondTabTextId?.let {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .clickable { onSelectionChanged?.invoke(false) }
                    .background(
                        color = if (isLeftSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = stringResource(id = it),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isLeftSelected) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    },
                )
            }
        }
    }
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun SpendingEditPageCategoriesPreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            SpendingEditPage(
                state = SpendingEditState(
                    spendingId = "",
                    categoryState = CategoriesState(
                        categoriesList = CategoriesMock.categoriesList,
                        selectedIndex = 1,
                        onSelectionChanged = {},
                        newCategoryName = "",
                        onNewCategoryNameChanged = { },
                        isSaveCategoryVisible = false,
                        onNewCategorySaved = { },
                        onCategoryPhotoUriChanged = { _, _ -> },
                        categoryPhotoChooserId = null,
                    ),
                    isCategoriesOpened = true,
                    onPageChanged = {},
                    namingText = "",
                    amountText = "",
                    dateText = "",
                    detailsList = emptyList(),
                    isCalendarOpen = false,
                    onCalendarVisibilityChanged = { },
                    onNamingTextChanged = {},
                    onAmountTextChanged = {},
                    onAddNewDetail = {},
                    onDetailNameChanged = { _, _ -> },
                    onDetailAmountChanged = { _, _ -> },
                    photoUri = null,
                    onPhotoUriChanged = { _ -> },
                    onSave = { },
                ),
                onPhotoRequest = {},
                onCategoryPhotoRequest = { _ -> },
            )
        }
    }
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun SpendingEditPageDetailsPreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            SpendingEditPage(
                state = SpendingEditState(
                    spendingId = "",
                    categoryState = CategoriesState(
                        categoriesList =
                        CategoriesMock.categoriesList,
                        selectedIndex = 1,
                        onSelectionChanged = {},
                        newCategoryName = "",
                        onNewCategoryNameChanged = { },
                        isSaveCategoryVisible = false,
                        onNewCategorySaved = { },
                        onCategoryPhotoUriChanged = { _, _ -> },
                        categoryPhotoChooserId = null,
                    ),
                    isCategoriesOpened = false,
                    onPageChanged = {},
                    namingText = "Food",
                    amountText = "19.50",
                    dateText = "19.10.2023",
                    detailsList = Mock.mockDetailsList,
                    isCalendarOpen = false,
                    onCalendarVisibilityChanged = {},
                    onNamingTextChanged = {},
                    onAmountTextChanged = {},
                    onAddNewDetail = {},
                    onDetailNameChanged = { _, _ -> },
                    onDetailAmountChanged = { _, _ -> },
                    photoUri = null,
                    onPhotoUriChanged = { _ -> },
                    onSave = {},
                ),
                onPhotoRequest = { },
                onCategoryPhotoRequest = { _ -> },
            )
        }
    }
}
