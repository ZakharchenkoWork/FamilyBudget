package com.faigenbloom.famillyspandings.ui.login

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.famillyspandings.comon.BaseDestination
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
            .loginStateFlow
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
