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
import com.faigenbloom.famillyspandings.ui.categories.CategoriesViewModel
import com.faigenbloom.famillyspandings.ui.spandings.DetailUiData
import org.koin.androidx.compose.koinViewModel


fun NavGraphBuilder.spendingEditPage(
    options: (
        showNavigation: Boolean,
        menuState: FloatingMenuState,
    ) -> Unit,
    onShowMessage: (MessageTypes) -> Unit,
    onPhotoRequest: (id: String) -> Unit,
    onCategoryPhotoRequest: (id: String) -> Unit,
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
        spendingsViewModel.onNext = onNext
        spendingsViewModel.onShowMessage = onShowMessage
        spendingsViewModel.onScreenTransition = {
            options(
                false,
                if (it) {
                    getCategoryMenuState()
                } else {
                    getEditSpendingMenuState()
                },
            )
        }
        spendingsViewModel.onCategoryIdLoaded = {
            categoriesViewModel.onCategoryIdLoaded(it)
        }
        categoriesViewModel.onCategorySelected = {
            spendingsViewModel.onCategorySelected(it)
        }

        val state by spendingsViewModel
            .stateFlow
            .collectAsState()

        options(
            false,
            if (state.isCategoriesOpened) {
                getCategoryMenuState()
            } else {
                getEditSpendingMenuState()
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
                        val uri: Uri? =
                            backStack.getPoppedArgument(PHOTO_KEY)
                        uri?.let {
                            categoryState.onCategoryPhotoUriChanged(
                                id ?: "",
                                it,
                            )
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

fun getCategoryMenuState(): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_plus,
        onMenuClick = {//TODO: ADD CATEGORY DIALOG
        },
    )
}

fun getEditSpendingMenuState(): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_options,
        listOf(
            MenuItemState(
                label = R.string.button_save,
                icon = R.drawable.icon_ok,
                onClick = {},
            ),
            MenuItemState(
                label = R.string.button_hide,
                icon = R.drawable.icon_hidden,
                onClick = {},
            ),
            MenuItemState(
                label = R.string.button_delete,
                icon = R.drawable.icon_delete,
                onClick = {},
            ),
        ),
    )
}

data object SpendingEditRoute : BaseDestination(
    route = "SpendingEditPage$SPENDING_ID_QUERY",
) {
    operator fun invoke(id: String = "") = if (id.isNotEmpty()) {
        route.replace(SPENDING_OPTIONAL_ID_KEY, id)
    } else {
        route.replace(SPENDING_ID_QUERY, "")
    }
}

