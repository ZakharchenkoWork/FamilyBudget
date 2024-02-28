package com.faigenbloom.familybudget.ui.spendings.edit

import android.net.Uri
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseDestination
import com.faigenbloom.familybudget.common.CALENDAR_START_DATE_ARG
import com.faigenbloom.familybudget.common.CATEGORY_PHOTO
import com.faigenbloom.familybudget.common.DETAILS_LIST_ARG
import com.faigenbloom.familybudget.common.FloatingMenuState
import com.faigenbloom.familybudget.common.ID_ARG
import com.faigenbloom.familybudget.common.MenuItemState
import com.faigenbloom.familybudget.common.PHOTO_KEY
import com.faigenbloom.familybudget.common.PHOTO_REASON_ARG
import com.faigenbloom.familybudget.common.SPENDING_ID_ARG
import com.faigenbloom.familybudget.common.SPENDING_ID_QUERY
import com.faigenbloom.familybudget.common.SPENDING_OPTIONAL_ID_KEY
import com.faigenbloom.familybudget.common.SPENDING_PHOTO
import com.faigenbloom.familybudget.common.fromJson
import com.faigenbloom.familybudget.common.getPoppedArgument
import com.faigenbloom.familybudget.ui.categories.CategoriesState
import com.faigenbloom.familybudget.ui.categories.CategoriesViewModel
import com.faigenbloom.familybudget.ui.categories.NO_INDEX
import com.faigenbloom.familybudget.ui.spendings.DetailUiData
import org.koin.androidx.compose.koinViewModel


fun NavGraphBuilder.spendingEditPage(
    bottomNavigationOptions: (Boolean) -> Unit,
    options: (menuState: FloatingMenuState) -> Unit,
    onPhotoRequest: (id: String) -> Unit,
    onCategoryPhotoRequest: (id: String?) -> Unit,
    onCalendarOpened: (String) -> Unit,
    onSpendingDialogRequest: (List<DetailUiData>) -> Unit,
    onNext: (String) -> Unit,
    onBack: () -> Unit,
) {
    composable(
        route = SpendingEditRoute.route,
        arguments = listOf(
            navArgument(SPENDING_ID_ARG) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
    ) { backStack ->

        val categoriesViewModel = koinViewModel<CategoriesViewModel>()
        val categoryState by categoriesViewModel
            .stateFlow.collectAsState()


        val spendingsViewModel = koinViewModel<SpendingEditViewModel>()
        val state by spendingsViewModel
            .stateFlow
            .collectAsState()

        spendingsViewModel.onNext = {
            if (it.isNotBlank())
                onNext(it)
            else
                onBack()
        }
        bottomNavigationOptions(false)
        spendingsViewModel.onScreenTransition = {
            options(
                if (it) {
                    getEditSpendingMenuState(state)
                } else {
                    getCategoryMenuState(state, categoryState)
                },
            )
        }
        spendingsViewModel.onCategoryIdLoaded = {
            categoriesViewModel.onCategoryIdLoaded(it)
        }
        categoriesViewModel.onCategorySelected = spendingsViewModel::onCategorySelected



        options(
            if (state.isInfoOpened) {
                getEditSpendingMenuState(state)
            } else {
                getCategoryMenuState(state, categoryState)
            },
        )
        backStack.getPoppedArgument(CALENDAR_START_DATE_ARG, "")?.let { calendarDate ->
            state.onDateChanged(calendarDate)
        }
        backStack.getPoppedArgument<String>(DETAILS_LIST_ARG, null)
            ?.let { wrappedDetails ->
                spendingsViewModel.updateDetail(
                    wrappedDetails.fromJson(),
                )
            }
        backStack.getPoppedArgument<String>(PHOTO_REASON_ARG)
            ?.let { reason ->
                val id: String? = backStack.getPoppedArgument(ID_ARG)
                when (reason) {
                    SPENDING_PHOTO -> {
                        if (id == state.spendingId) {
                            state.onPhotoUriChanged(
                                backStack.getPoppedArgument(PHOTO_KEY),
                            )
                        } else {
                        }
                    }
                    CATEGORY_PHOTO -> {
                        backStack.getPoppedArgument<Uri>(PHOTO_KEY, null)?.let {
                            categoryState.onCategoryPhotoUriChanged(it)
                        }
                    }
                    else -> {}
                }
            }

        SpendingEditPage(
            state = state,
            categoryState = categoryState,
            onPhotoRequest = onPhotoRequest,
            onCategoryPhotoRequest = onCategoryPhotoRequest,
            onCalendarOpened = onCalendarOpened,
            onSpendingDialogRequest = onSpendingDialogRequest,
            onBack = onBack,
        )
    }
}

fun getCategoryMenuState(
    state: SpendingEditState,
    categoryState: CategoriesState,
): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_plus,
        onMenuClick = {
            categoryState.onCategoryDialogVisibilityChanged(NO_INDEX)
        },
        alwaysVisibleButton = MenuItemState(
            label = R.string.button_save,
            icon = if (state.isOkActive) {
                R.drawable.icon_ok
            } else {
                R.drawable.icon_ok_inactive
            },
            semantics = SPENDING_SAVE_BUTTON,
            onClick = state.onSave,
        ),
    )
}

fun getEditSpendingMenuState(state: SpendingEditState): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_options,
        items = listOf(
            MenuItemState(
                label = if (state.isPlanned) {
                    R.string.button_spent
                } else {
                    R.string.button_planned
                },
                icon = if (state.isPlanned) {
                    R.drawable.icon_list_planned_outlined
                } else {
                    R.drawable.icon_list_outlined
                },
                onClick = state.onPlannedChanged,
            ),
            MenuItemState(
                label = if (state.isHidden) {
                    R.string.button_show
                } else {
                    R.string.button_hide
                },
                icon = if (state.isHidden) {
                    R.drawable.icon_shown
                } else {
                    R.drawable.icon_hidden
                },
                onClick = state.onHideChanged,
            ),
            MenuItemState(
                label = R.string.button_delete,
                icon = R.drawable.icon_delete,
                onClick = state.deleteSpending,
            ),
            MenuItemState(
                isShown = state.canDuplicate,
                label = R.string.button_duplicate,
                icon = R.drawable.icon_duplicate,
                onClick = state.onDuplicate,
            ),
        ),
        alwaysVisibleButton = MenuItemState(
            label = R.string.button_save,
            icon = if (state.isOkActive) {
                R.drawable.icon_ok
            } else {
                R.drawable.icon_ok_inactive
            },
            semantics = SPENDING_SAVE_BUTTON,
            onClick = state.onSave,
        ),
    )
}

data object SpendingEditRoute : BaseDestination(
    route = "SpendingEditPage$SPENDING_ID_QUERY",
) {
    operator fun invoke(id: String = "") = if (id.isNotBlank()) {
        route.replace(SPENDING_OPTIONAL_ID_KEY, id)
    } else {
        route.replace(SPENDING_ID_QUERY, "")
    }
}

