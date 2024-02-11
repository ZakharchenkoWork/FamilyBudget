package com.faigenbloom.familybudget.ui.budget

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseDestination
import com.faigenbloom.familybudget.common.FloatingMenuState
import com.faigenbloom.familybudget.common.MenuItemState
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.budgetPage(
    bottomNavigationOptions: (
        showNavigation: Boolean,
        index: Int,
    ) -> Unit,
    options: (
        menuState: FloatingMenuState,
    ) -> Unit,
) {
    composable(
        route = BudgetRoute(),
    ) {
        bottomNavigationOptions(true, 1)
        val state by koinViewModel<BudgetPageViewModel>().stateFlow.collectAsState()
        options(getBudgetMenuState(state))
        BudgetPage(
            state = state,
        )
    }
}

fun getBudgetMenuState(
    state: BudgetState,
): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_filter,
        items = listOf(
            MenuItemState(
                label = R.string.spendings_filter_yearly,
                onClick = state.yearlyClicked,
            ),
            MenuItemState(
                label = R.string.spendings_filter_monthly,
                onClick = state.monthlyClicked,
            ),
        ),
        alwaysVisibleButton = MenuItemState(
            isShown = state.isSaveVisible,
            label = R.string.button_save,
            icon = R.drawable.icon_ok,
            onClick = state.onSave,
        ),
    )
}
data object BudgetRoute : BaseDestination(
    route = "BudgetRoute",
) {
    operator fun invoke() = route
}
