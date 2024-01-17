package com.faigenbloom.famillyspandings.ui.register

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.famillyspandings.comon.BaseDestination
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.registerPage(
    onLoggedIn: () -> Unit,
    onBack: () -> Unit,
) {
    composable(
        route = RegisterRoute.route,
    ) {
        val registerPageViewModel = koinViewModel<RegisterPageViewModel>()
        registerPageViewModel.onLoggedIn = onLoggedIn
        val state by registerPageViewModel
            .loginStateFlow
            .collectAsState()
        RegisterPage(
            state = state,
            onBack = onBack,
        )
    }
}

data object RegisterRoute : BaseDestination(route = "Register") {
    operator fun invoke() = route
}
