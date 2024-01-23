package com.faigenbloom.famillyspandings.ui.spendings.show

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.common.BaseDestination
import com.faigenbloom.famillyspandings.common.FloatingMenuState
import com.faigenbloom.famillyspandings.common.ID_ARG
import com.faigenbloom.famillyspandings.common.ID_KEY
import com.faigenbloom.famillyspandings.common.MenuItemState
import org.koin.androidx.compose.koinViewModel


fun NavGraphBuilder.spendingShowPage(
    bottomNavigationOptions: (Boolean) -> Unit,
    options: (menuState: FloatingMenuState) -> Unit,
    onEditClicked: (String) -> Unit,
    onBack: () -> Unit,
) {
    composable(
        route = SpendingShowRoute.route,
        arguments = listOf(
            navArgument(ID_ARG) {
                type = NavType.StringType
            },
        ),
    ) {
        bottomNavigationOptions(false)

        val viewModel = koinViewModel<SpendingShowViewModel>()

        viewModel.onEditSpending = onEditClicked

        val state by viewModel
            .spendingsStateFlow
            .collectAsState()

        SpendingShowPage(
            state = state,
            onBack = onBack,
        )

        options(getShowSpendingMenuState(state))
    }
}

fun getShowSpendingMenuState(state: SpendingShowState): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_edit,
        items = listOf(
            MenuItemState(
                isShown = state.isPlanned,
                label = R.string.spending_mark_purchased,
                icon = R.drawable.icon_buy,
                onClick = state.onMarkPurchasedClicked,
            ),
            MenuItemState(
                label = R.string.button_duplicate,
                icon = R.drawable.icon_duplicate,
                onClick = state.onDuplicateClicked,
            ),
            MenuItemState(
                label = R.string.button_edit,
                icon = R.drawable.icon_edit,
                onClick = state.onEditClicked,
            ),
        ),
    )
}

data object SpendingShowRoute : BaseDestination(
    route = "SpendingShowRoute/$ID_KEY",
) {
    operator fun invoke(id: String) =
        route.replace(ID_KEY, id)
}
