package com.faigenbloom.familybudget.ui.onboarding

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.familybudget.common.BaseDestination
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.onboardingPage(
    onLogin: () -> Unit,
    onRegister: (String?) -> Unit,
    onLoggedIn: () -> Unit,
) {
    composable(
        route = OnboardingRoute(),
    ) {
        val qrLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
            if (result is QRResult.QRSuccess) {
                result.content.rawValue?.let {
                    onRegister(it)
                }
            }
        }
        val viewModel = koinViewModel<OnboardingViewModel>()
        viewModel.onAuthPassed = onLoggedIn
        val state by viewModel.stateFlow.collectAsState()

        OnboardingPage(
            state = state,
            onLogin = onLogin,
            onRegister = { onRegister(null) },
            onQrScan = {
                qrLauncher.launch(null)
            },
        )
    }
}

data object OnboardingRoute : BaseDestination(route = "Onboarding") {
    operator fun invoke() = route
}
