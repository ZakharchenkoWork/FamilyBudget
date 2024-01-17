package com.faigenbloom.famillyspandings.ui.spandings.show

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faigenbloom.famillyspandings.comon.BaseDestination
import com.faigenbloom.famillyspandings.comon.ID_ARG
import com.faigenbloom.famillyspandings.comon.ID_KEY
import org.koin.androidx.compose.koinViewModel


fun NavGraphBuilder.spendingShowPage(
    bottomNavigationOptions: (
        showNavigation: Boolean,
        index: Int,
    ) -> Unit,
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
        bottomNavigationOptions(false, 0)

        val state by koinViewModel<SpendingShowViewModel>()
            .spendingsStateFlow
            .collectAsState()

        SpendingShowPage(
            state = state,
            onEditClicked = onEditClicked,
            onBack = onBack,
        )
    }
}

data object SpendingShowRoute : BaseDestination(
    route = "SpendingShowPage/$ID_KEY",
) {
    operator fun invoke(id: String) =
        route.replace(ID_KEY, id)
}
