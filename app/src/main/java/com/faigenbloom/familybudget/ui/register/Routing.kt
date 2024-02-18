package com.faigenbloom.familybudget.ui.register

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faigenbloom.familybudget.common.BaseDestination
import com.faigenbloom.familybudget.common.ID_KEY_QUERY
import com.faigenbloom.familybudget.common.OPTIONAL_ID_ARG
import com.faigenbloom.familybudget.common.OPTIONAL_ID_KEY
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.registerPage(
    onLoggedIn: () -> Unit,
    onBack: () -> Unit,
) {
    composable(
        route = RegisterRoute.route,
        arguments = listOf(
            navArgument(OPTIONAL_ID_ARG) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
    ) { backStackEntry ->
        val registerPageViewModel = koinViewModel<RegisterPageViewModel>()
        registerPageViewModel.onRegistered = onLoggedIn
        registerPageViewModel.forFamily(backStackEntry.arguments?.getString(OPTIONAL_ID_ARG))
        val state by registerPageViewModel
            .stateFlow
            .collectAsState()
        RegisterPage(
            state = state,
            onBack = onBack,
        )
    }
}

data object RegisterRoute : BaseDestination(route = "Register/$ID_KEY_QUERY") {
    operator fun invoke(id: String? = null): String {
        return route.replace(OPTIONAL_ID_KEY, id ?: "")
    }
}
