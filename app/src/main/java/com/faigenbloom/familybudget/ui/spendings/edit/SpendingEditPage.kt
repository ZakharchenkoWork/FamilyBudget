package com.faigenbloom.familybudget.ui.spendings.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseTextField
import com.faigenbloom.familybudget.common.StripeBar
import com.faigenbloom.familybudget.common.TextFieldType
import com.faigenbloom.familybudget.common.TopBar
import com.faigenbloom.familybudget.common.ui.AnimateError
import com.faigenbloom.familybudget.common.ui.AnimateTabs
import com.faigenbloom.familybudget.common.ui.Loading
import com.faigenbloom.familybudget.common.ui.MoneyTextTransformation
import com.faigenbloom.familybudget.ui.categories.CategoriesPage
import com.faigenbloom.familybudget.ui.categories.CategoriesState
import com.faigenbloom.familybudget.ui.categories.mockCategoriesList
import com.faigenbloom.familybudget.ui.spendings.DetailUiData
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import com.faigenbloom.familybudget.ui.theme.hint
import java.util.Currency
import java.util.Locale

@Composable
fun SpendingEditPage(
    state: SpendingEditState,
    categoryState: CategoriesState,
    onPhotoRequest: (id: String) -> Unit,
    onCategoryPhotoRequest: (id: String?) -> Unit,
    onCalendarOpened: (String) -> Unit,
    onSpendingDialogRequest: (List<DetailUiData>) -> Unit,
    onBack: () -> Unit,
) {
    Column {
        TopBar(
            title = if (state.spendingId.isBlank()) {
                stringResource(R.string.adding_spending_title)
            } else if (state.isDuplicate) {
                stringResource(R.string.editing_spending_duplicate)
            } else if (state.isPlanned) {
                stringResource(R.string.editing_planned_spending_title)
            } else if (state.isHidden) {
                stringResource(R.string.editing_hidden_spending_title)
            } else if (state.isCurrentUserOwner) {
                stringResource(R.string.editing_spending_title)
            } else {
                stringResource(R.string.editing_another_user_spending_title, state.ownerName)
            },
            startIcon = R.drawable.icon_arrow_back,
            onStartIconCLicked = onBack,
        )
        StripeBar(
            textId = R.string.spending_information,
            secondTabTextId = R.string.choose_category,
            isLeftSelected = state.isInfoOpened,
            onSelectionChanged = state.onPageChanged,
        )

        AnimateTabs(isLeftTab = state.isInfoOpened) { isInfoOpened ->
            if (isInfoOpened) {
                Information(
                    state = state,
                    onPhotoRequest = onPhotoRequest,
                    onCalendarOpened = onCalendarOpened,
                    onSpendingDialogRequest = { onSpendingDialogRequest(state.detailsList) },
                )
            } else {
                categoryState.onCategoryError(state.isCategoryError)
                CategoriesPage(
                    state = categoryState,
                    onCategoryFinished = {
                        state.onResetErrors()
                        categoryState.onCategoryError(false)
                    },
                    onCategoryPhotoRequest = onCategoryPhotoRequest,
                )
            }
        }
    }
    val isLoadingCategory by remember { categoryState.isLoading }
    val isLoading by remember { state.isLoading }
    Loading(isShown = isLoading || isLoadingCategory)
}

@Composable
fun Information(
    state: SpendingEditState,
    onPhotoRequest: (id: String) -> Unit,
    onCalendarOpened: (String) -> Unit,
    onSpendingDialogRequest: (String) -> Unit,
) {
    Column {
        TopInfo(onPhotoRequest, state, onCalendarOpened)
        SpacerStripe(
            modifier = Modifier.padding(top = 16.dp),
            state = state,
        )
        DetailsList(
            state = state,
            onSpendingDialogRequest = {
                onSpendingDialogRequest("")
            },
        )
    }
}

@Composable
private fun TopInfo(
    onPhotoRequest: (id: String) -> Unit,
    state: SpendingEditState,
    onCalendarOpened: (String) -> Unit,
) {
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
                } ?: painterResource(id = R.drawable.icon_photo),
                contentDescription = "",
            )
        }
        Column(
            modifier = Modifier.weight(0.5f),
            verticalArrangement = Arrangement.Center,
        ) {
            AnimateError(
                animationTrigger = state.isNameError,
                onFinished = state.onResetErrors,
            ) { backgroundColor ->
                BaseTextField(
                    modifier = Modifier
                        .background(
                            color = backgroundColor,
                        )
                        .semantics {
                            contentDescription = SPENDING_NAME_INPUT
                        },
                    text = state.namingText,
                    labelId = R.string.spending_details_name,
                    onTextChange = state.onNamingTextChanged,
                )
            }
            AnimateError(
                animationTrigger = state.isAmountError,
                onFinished = state.onResetErrors,
            ) { backgroundColor ->
                BaseTextField(
                    modifier = Modifier
                        .background(
                            color = backgroundColor,
                        )
                        .semantics {
                            contentDescription = SPENDING_AMOUNT_INPUT
                        },
                    text = state.amountText,
                    labelId = R.string.spending_details_amount,
                    textFieldType = TextFieldType.Money(state.currency),
                    onTextChange = state.onAmountTextChanged,
                )
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 4.dp)
                    .clickable {
                        onCalendarOpened(state.dateText)
                    },
                text = state.dateText.ifEmpty {
                    stringResource(
                        id = R.string.date,
                    )
                },
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun DetailsList(
    state: SpendingEditState,
    onSpendingDialogRequest: () -> Unit,
) {
    LazyColumn {
        item {
            AddItemLine(
                onSpendingDialogRequest = onSpendingDialogRequest,
            )
        }
        items(state.detailsList.size) { detailIndex ->
            DetailsItem(
                state = state,
                currencyCode = state.currency.currencyCode,
                detailIndex = detailIndex,
            )
        }
    }
}

@Composable
fun AddItemLine(onSpendingDialogRequest: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .semantics { contentDescription = ADD_DETAIL_BUTTON }
                .clickable {
                    onSpendingDialogRequest()
                },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_plus),
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
    currencyCode: String,
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
            Text(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 8.dp)
                    .semantics { contentDescription = SPENDING_DETAIL_NAME + detailIndex },
                text = spendingDetail.name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 8.dp)
                    .semantics { contentDescription = SPENDING_DETAIL_AMOUNT + detailIndex },
                textAlign = TextAlign.End,
                text = MoneyTextTransformation(currencyCode)
                    .filter(spendingDetail.amount),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
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
    state: SpendingEditState,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
            ),
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(16.dp),
                text = stringResource(id = R.string.spending_details),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Row(
                modifier = Modifier
                    .weight(0.5f),
                horizontalArrangement = Arrangement.End,
                ) {
                if (state.isHidden) {
                    Image(
                        modifier = Modifier
                            .height(32.dp)
                            .aspectRatio(1f)
                            .padding(end = 16.dp),
                        painter = painterResource(id = R.drawable.icon_hidden),
                        contentDescription = "",
                    )
                }
                if (state.isPlanned) {
                    Image(
                        modifier = Modifier
                            .height(32.dp)
                            .aspectRatio(1f)
                            .padding(end = 16.dp),
                        painter = painterResource(id = R.drawable.icon_list_planned_outlined),
                        contentDescription = "",
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpendingEditPageCategoriesPreview() {
    FamillySpandingsTheme {
        Surface {
            SpendingEditPage(
                state = SpendingEditState(
                    spendingId = "asdf",
                    isInfoOpened = true,
                    onPageChanged = {},
                    namingText = "",
                    isNameError = false,
                    amountText = "",
                    dateText = "",
                    detailsList = emptyList(),
                    currency = Currency.getInstance(Locale.getDefault()),
                    isHidden = false,
                    isDuplicate = false,
                    isPlanned = false,
                    isCurrentUserOwner = false,
                    ownerName = "Natalia",
                    onResetErrors = {},
                    onNamingTextChanged = {},
                    onAmountTextChanged = {},
                    photoUri = null,
                    onPhotoUriChanged = { _ -> },
                    onSave = { },
                    onDateChanged = { },
                    isOkActive = false,
                    onHideChanged = {},
                    deleteSpending = { },
                    canDuplicate = false,
                    onDuplicate = {},
                    onPlannedChanged = {},
                    isAmountError = false,
                    isCategoryError = false,
                ),
                categoryState = CategoriesState(
                    categoriesList = mockCategoriesList,
                    selectedIndex = 1,
                    onSelectionChanged = {},
                    newCategoryName = "",
                    onNewCategoryNameChanged = { },
                    isSaveCategoryVisible = false,
                    onNewCategorySaved = { },
                    onCategoryPhotoUriChanged = { },
                    categoryPhotoChooserId = null,
                    newCategoryPhoto = null,
                    isEditCategoryShown = false,
                    onCategoryDialogVisibilityChanged = { },
                    categoryId = null,
                    onDeleteCategory = {},
                    isCategoryError = false,
                    onCategoryError = {},
                    ),
                onPhotoRequest = {},
                onCategoryPhotoRequest = { _ -> },
                onCalendarOpened = { },
                onSpendingDialogRequest = {},
                onBack = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpendingEditPageDetailsPreview() {
    FamillySpandingsTheme {
        Surface {
            SpendingEditPage(
                state = SpendingEditState(
                    spendingId = "",
                    isInfoOpened = false,
                    onPageChanged = {},
                    namingText = "Food",
                    amountText = "19.50",
                    isNameError = false,
                    dateText = "19.10.2023",
                    detailsList = mockDetailsList,
                    onResetErrors = {},
                    onNamingTextChanged = {},
                    onAmountTextChanged = {},
                    photoUri = null,
                    isDuplicate = true,
                    isCurrentUserOwner = true,
                    isPlanned = true,
                    currency = Currency.getInstance(Locale.getDefault()),
                    onPhotoUriChanged = { _ -> },
                    onSave = {},
                    onDateChanged = { },
                    isOkActive = true,
                    isHidden = true,
                    onHideChanged = {},
                    deleteSpending = { },
                    canDuplicate = false,
                    onDuplicate = {},
                    onPlannedChanged = {},
                    isAmountError = false,
                    isCategoryError = false,
                ),
                categoryState = CategoriesState(
                    categoriesList =
                    mockCategoriesList,
                    selectedIndex = 1,
                    onSelectionChanged = {},
                    newCategoryName = "",
                    onNewCategoryNameChanged = { },
                    isSaveCategoryVisible = false,
                    onNewCategorySaved = { },
                    onCategoryPhotoUriChanged = { },
                    categoryPhotoChooserId = null,
                    newCategoryPhoto = null,
                    isEditCategoryShown = false,
                    onCategoryDialogVisibilityChanged = { },
                    categoryId = null,
                    onDeleteCategory = { },
                    isCategoryError = false,
                    onCategoryError = {},
                ),
                onPhotoRequest = {},
                onCategoryPhotoRequest = { _ -> },
                onCalendarOpened = {},
                onSpendingDialogRequest = {},
                onBack = {},
            )
        }
    }
}
