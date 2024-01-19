package com.faigenbloom.famillyspandings.ui.spandings.edit

import android.net.Uri
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.BaseDestination
import com.faigenbloom.famillyspandings.comon.CATEGORY_PHOTO
import com.faigenbloom.famillyspandings.comon.DATE
import com.faigenbloom.famillyspandings.comon.DETAILS_LIST_ARG
import com.faigenbloom.famillyspandings.comon.FloatingMenuState
import com.faigenbloom.famillyspandings.comon.ID_ARG
import com.faigenbloom.famillyspandings.comon.MenuItemState
import com.faigenbloom.famillyspandings.comon.PHOTO_KEY
import com.faigenbloom.famillyspandings.comon.PHOTO_REASON_ARG
import com.faigenbloom.famillyspandings.comon.SPENDING_ID_ARG
import com.faigenbloom.famillyspandings.comon.SPENDING_ID_QUERY
import com.faigenbloom.famillyspandings.comon.SPENDING_OPTIONAL_ID_KEY
import com.faigenbloom.famillyspandings.comon.SPENDING_PHOTO
import com.faigenbloom.famillyspandings.comon.fromJson
import com.faigenbloom.famillyspandings.comon.getPoppedArgument
import com.faigenbloom.famillyspandings.ui.categories.CategoriesState
import com.faigenbloom.famillyspandings.ui.categories.CategoriesViewModel
import com.faigenbloom.famillyspandings.ui.categories.NO_INDEX
import com.faigenbloom.famillyspandings.ui.spandings.DetailUiData
import org.koin.androidx.compose.koinViewModel


fun NavGraphBuilder.spendingEditPage(
    options: (
        showNavigation: Boolean,
        menuState: FloatingMenuState,
    ) -> Unit,
    onShowMessage: (MessageTypes) -> Unit,
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
        spendingsViewModel.onShowMessage = onShowMessage
        spendingsViewModel.onScreenTransition = {
            options(
                false,
                if (it) {
                    getCategoryMenuState(categoryState)
                } else {
                    getEditSpendingMenuState(state)
                },
            )
        }
        spendingsViewModel.onCategoryIdLoaded = {
            categoriesViewModel.onCategoryIdLoaded(it)
        }
        categoriesViewModel.onCategorySelected = spendingsViewModel::onCategorySelected



        options(
            false,
            if (state.isCategoriesOpened) {
                getCategoryMenuState(categoryState)
            } else {
                getEditSpendingMenuState(state)
            },
        )
        backStack.getPoppedArgument(DATE, "")?.let { calendarDate ->
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

fun getCategoryMenuState(categoryState: CategoriesState): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_plus,
        onMenuClick = {
            categoryState.onCategoryDialogVisibilityChanged(NO_INDEX)
        },
    )
}

fun getEditSpendingMenuState(state: SpendingEditState): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_options,
        listOf(
            MenuItemState(
                label = R.string.button_save,
                icon = if (state.isOkActive) {
                    R.drawable.icon_ok
                } else {
                    R.drawable.icon_ok_inactive
                },
                semantics = SPENDING_SAVE_BUTTON,
                onClick = state.onSave,

                ),
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

