package com.faigenbloom.famillyspandings.ui.budget

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.famillyspandings.common.BaseDestination
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.budgetPage(
    bottomNavigationOptions: (
        showNavigation: Boolean,
        index: Int,
    ) -> Unit,
    onAddSpendingClicked: () -> Unit,
    onAddPlannedSpendingClicked: () -> Unit,
) {
    composable(
        route = BudgetRoute(),
    ) {
        bottomNavigationOptions(true, 1)
        val state by koinViewModel<BudgetPageViewModel>().budgetStateFlow.collectAsState()

        BudgetPage(
            state = state,
            onAddSpendingClicked = onAddSpendingClicked,
            onAddPlannedSpendingClicked = onAddPlannedSpendingClicked,
        )
    }
}

data object BudgetRoute : BaseDestination(
    route = "BudgetRoute",
) {
    operator fun invoke() = route
}