package com.faigenbloom.familybudget.ui.login

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.familybudget.common.BaseDestination
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.loginPage(
    onLoggedIn: () -> Unit,
    onBack: () -> Unit,
) {
    composable(
        route = LoginRoute(),
    ) {
        val loginPageViewModel = koinViewModel<LoginPageViewModel>()
        loginPageViewModel.onLoggedIn = onLoggedIn
        val state by loginPageViewModel
            .stateFlow
            .collectAsState()
        LoginPage(
            state = state,
            onBack = onBack,
        )
    }
}

data object LoginRoute : BaseDestination(route = "Login") {
    operator fun invoke() = route
}
