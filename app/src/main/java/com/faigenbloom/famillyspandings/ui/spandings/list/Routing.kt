package com.faigenbloom.famillyspandings.ui.spandings.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.BaseDestination
import com.faigenbloom.famillyspandings.comon.FloatingMenuState
import com.faigenbloom.famillyspandings.comon.MenuItemState
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.spendingsListPage(
    padding: PaddingValues,
    options: (
        showNavigation: Boolean,
        index: Int,
        menuState: FloatingMenuState,
    ) -> Unit,
    onOpenSpending: (String) -> Unit,
) {
    composable(
        route = SpendingsListPage.route,
    ) {
        val viewModel = koinViewModel<SpendingsPageViewModel>()
        val state by viewModel
            .spendingsStateFlow
            .collectAsState()
        options(
            true, 0,
            getMainMenu(state, options),
        )
        viewModel.reloadData()
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
    options: (
        showNavigation: Boolean,
        index: Int,
        menuState: FloatingMenuState,
    ) -> Unit,
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
                label = R.string.spendings_filter,
                icon = R.drawable.icon_filter,
                onClick = {
                    options(true, 0, getFilterMenu())
                },
            ),
        ),
    )
}

fun getFilterMenu(): FloatingMenuState {
    return FloatingMenuState(
        R.drawable.icon_filter,
        listOf(
            MenuItemState(
                label = R.string.spendings_filter,
                icon = R.drawable.icon_list_outlined,
                onClick = {

                },
            ),
        ),
    )
}

data object SpendingsListPage : BaseDestination(
    route = "SpendingsPage",
) {
    operator fun invoke() = route
}
