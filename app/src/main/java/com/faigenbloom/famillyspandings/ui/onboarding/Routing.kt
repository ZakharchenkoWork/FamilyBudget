package com.faigenbloom.famillyspandings.ui.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.famillyspandings.comon.BaseDestination

fun NavGraphBuilder.onboardingPage(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
) {
    composable(
        route = OnboardingRoute(),
    ) {
        OnboardingPage(
            onLogin = onLogin,
            onRegister = onRegister,
        )
    }
}

data object OnboardingRoute : BaseDestination(route = "Onboarding") {
    operator fun invoke() = route
}
