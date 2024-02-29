package com.faigenbloom.familybudget.ui.spendings.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseDestination
import com.faigenbloom.familybudget.common.CALENDAR_END_DATE_ARG
import com.faigenbloom.familybudget.common.CALENDAR_START_DATE_ARG
import com.faigenbloom.familybudget.common.FloatingMenuState
import com.faigenbloom.familybudget.common.MenuItemState
import com.faigenbloom.familybudget.common.getPoppedArgument
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.spendingsListPage(
    padding: PaddingValues,
    options: (
        showNavigation: Boolean,
        index: Int,
    ) -> Unit,
    menuCallback: (menuState: FloatingMenuState) -> Unit,
    onCalendarRequested: ((fromDate: String, toDate: String) -> Unit),
    onOpenSpending: (String) -> Unit,
) {
    composable(
        route = SpendingsListPage.route,
    ) { backStack ->
        val viewModel = koinViewModel<SpendingsListViewModel>()
        viewModel.onCalendarRequested = onCalendarRequested

        viewModel.onDateRangeChanged(
            backStack.getPoppedArgument(CALENDAR_START_DATE_ARG, "") ?: "",
            backStack.getPoppedArgument(CALENDAR_END_DATE_ARG, "") ?: "",
        )
        val state by viewModel
            .stateFlow
            .collectAsState()
        options(true, 0)
        menuCallback(
            getMainMenu(
                state = state,
                menuCallback = menuCallback,
            ),
        )

        SpendingsListPage(
            modifier = Modifier.padding(
                bottom = padding.calculateBottomPadding(),
            ),
            state = state,
            onOpenSpending = onOpenSpending,
        )
    }
}

fun getMainMenu(
    state: SpendingsState,
    menuCallback: (menuState: FloatingMenuState) -> Unit,
): FloatingMenuState {
    return FloatingMenuState(
        R.drawable.icon_options,
        listOf(
            MenuItemState(
                label = if (state.filterType.isPlanned) {
                    R.string.spendings_previous_title
                } else {
                    R.string.spendings_planned_title
                },
                icon = if (state.filterType.isPlanned) {
                    R.drawable.icon_list_outlined
                } else {
                    R.drawable.icon_list_planned_outlined
                },
                onClick = state.onPlannedSwitched,
            ),
            MenuItemState(
                label = R.string.spendings_filter_title,
                icon = R.drawable.icon_filter,
                onClick = {
                    menuCallback(
                        getFilterMenu(
                            state = state,
                            onClose = {
                                menuCallback(
                                    getMainMenu(
                                        state,
                                        menuCallback,
                                    ),
                                )
                            },
                        ),
                    )
                },
            ),
        ),
    )
}

fun getFilterMenu(
    state: SpendingsState,
    onClose: () -> Unit,
): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_filter,
        onMenuClick = onClose,
        items = listOf(
            MenuItemState(
                label = R.string.spendings_filter_range,
                onClick = state.onRangeFiltered,
            ),
            MenuItemState(
                label = R.string.spendings_filter_yearly,
                onClick = state.onYearlyFiltered,
            ),
            MenuItemState(
                label = R.string.spendings_filter_monthly,
                onClick = state.onMonthlyFiltered,
            ),
            MenuItemState(
                label = R.string.spendings_filter_daily,
                onClick = state.onDailyFiltered,
            ),
        ),
    )
}

data object SpendingsListPage : BaseDestination(
    route = "SpendingsPage",
) {
    operator fun invoke() = route
}
