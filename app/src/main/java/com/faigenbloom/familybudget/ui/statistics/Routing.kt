package com.faigenbloom.familybudget.ui.statistics

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

fun NavGraphBuilder.statisticsPage(
    bottomNavigationOptions: (showNavigation: Boolean, index: Int) -> Unit,
    options: (
        menuState: FloatingMenuState,
    ) -> Unit,
    onCalendarRequested: ((fromDate: String, toDate: String) -> Unit),
) {
    composable(
        route = StatisticsRoute(),
    ) { backStack ->
        bottomNavigationOptions(true, 3)
        val viewModel = koinViewModel<StatisticsPageViewModel>()
        val state by viewModel.stateFlow.collectAsState()
        viewModel.onCalendarRequested = onCalendarRequested
        viewModel.onDateRangeChanged(
            backStack.getPoppedArgument(CALENDAR_START_DATE_ARG, "") ?: "",
            backStack.getPoppedArgument(CALENDAR_END_DATE_ARG, "") ?: "",
        )
        options(getStatistcsMenuState(state))

        StatisticsPage(state)
    }
}

fun getStatistcsMenuState(
    state: StatisicsState,
): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_filter,
        items = listOf(
            MenuItemState(
                label = R.string.spendings_filter_range,
                onClick = state.rangeClicked,
            ),
            MenuItemState(
                label = R.string.spendings_filter_yearly,
                onClick = state.yearlyClicked,
            ),
            MenuItemState(
                label = R.string.spendings_filter_monthly,
                onClick = state.monthlyClicked,
            ),
            MenuItemState(
                label = R.string.spendings_filter_daily,
                onClick = state.dailyClicked,
            ),
        ),
    )
}

data object StatisticsRoute : BaseDestination(route = "StatisticsRoute") {
    operator fun invoke() = route
}
