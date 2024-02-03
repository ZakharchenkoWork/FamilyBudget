package com.faigenbloom.familybudget.ui.onboarding

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.familybudget.common.BaseDestination
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.onboardingPage(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onLoggedIn: () -> Unit,
) {
    composable(
        route = OnboardingRoute(),
    ) {
        val viewModel = koinViewModel<OnboardingViewModel>()
        viewModel.onAuthPassed = onLoggedIn
        val state by viewModel.stateFlow.collectAsState()

        OnboardingPage(
            onLogin = onLogin,
            onRegister = onRegister,
        )
    }
}

data object OnboardingRoute : BaseDestination(route = "Onboarding") {
    operator fun invoke() = route
}
