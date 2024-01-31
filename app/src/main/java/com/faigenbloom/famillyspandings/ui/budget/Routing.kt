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
) {
    composable(
        route = BudgetRoute(),
    ) {
        bottomNavigationOptions(true, 1)
        val state by koinViewModel<BudgetPageViewModel>().stateFlow.collectAsState()

        BudgetPage(
            state = state,
        )
    }
}

data object BudgetRoute : BaseDestination(
    route = "BudgetRoute",
) {
    operator fun invoke() = route
}
