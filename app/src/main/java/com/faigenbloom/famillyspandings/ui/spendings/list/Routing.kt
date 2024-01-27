package com.faigenbloom.famillyspandings.ui.spendings.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.common.BaseDestination
import com.faigenbloom.famillyspandings.common.CALENDAR_END_DATE_ARG
import com.faigenbloom.famillyspandings.common.CALENDAR_START_DATE_ARG
import com.faigenbloom.famillyspandings.common.FloatingMenuState
import com.faigenbloom.famillyspandings.common.MenuItemState
import com.faigenbloom.famillyspandings.common.getPoppedArgument
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
            .spendingsStateFlow
            .collectAsState()
        options(true, 0)
        menuCallback(
            getMainMenu(
                state = state,
                menuCallback = menuCallback,
                rangeClicked = viewModel::calendarRequest,
                dailyClicked = viewModel::onDailyFiltered,
                monthlyClicked = viewModel::onMonthlyFiltered,
                yearlyClicked = viewModel::onYearlyFiltered,
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
    rangeClicked: () -> Unit,
    yearlyClicked: () -> Unit,
    monthlyClicked: () -> Unit,
    dailyClicked: () -> Unit,
): FloatingMenuState {
    return FloatingMenuState(
        R.drawable.icon_options,
        listOf(
            MenuItemState(
                label = if (state.isPlannedListShown) {
                    R.string.spendings_previous_title
                } else {
                    R.string.spendings_planned_title
                },
                icon = if (state.isPlannedListShown) {
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
                            rangeClicked = rangeClicked,
                            monthlyClicked = monthlyClicked,
                            yearlyClicked = yearlyClicked,
                            dailyClicked = dailyClicked,
                            onClose = {
                                menuCallback(
                                    getMainMenu(
                                        state,
                                        menuCallback,
                                        rangeClicked,
                                        yearlyClicked,
                                        monthlyClicked,
                                        dailyClicked,
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
    rangeClicked: () -> Unit,
    yearlyClicked: () -> Unit,
    monthlyClicked: () -> Unit,
    dailyClicked: () -> Unit,
    onClose: () -> Unit,
): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_filter,
        onMenuClick = onClose,
        items = listOf(
            MenuItemState(
                label = R.string.spendings_filter_range,
                onClick = rangeClicked,
            ),
            MenuItemState(
                label = R.string.spendings_filter_yearly,
                onClick = yearlyClicked,
            ),
            MenuItemState(
                label = R.string.spendings_filter_monthly,
                onClick = monthlyClicked,
            ),
            MenuItemState(
                label = R.string.spendings_filter_daily,
                onClick = dailyClicked,
            ),
        ),
    )
}

data object SpendingsListPage : BaseDestination(
    route = "SpendingsPage",
) {
    operator fun invoke() = route
}
