package com.faigenbloom.familybudget.ui.family

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseDestination
import com.faigenbloom.familybudget.common.FloatingMenuState
import com.faigenbloom.familybudget.common.QR_KEY
import com.faigenbloom.familybudget.common.getPoppedArgument
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.familyPage(
    bottomNavigationOptions: (
        showNavigation: Boolean,
    ) -> Unit,
    options: (
        menuState: FloatingMenuState,
    ) -> Unit,
    onQRScanRequested: () -> Unit,
    onBack: () -> Unit,
) {

    composable(
        route = FamilyRoute(),
    ) {
        bottomNavigationOptions(false)

        val state by koinViewModel<FamilyPageViewModel>().stateFlow.collectAsState()
        state.onQrScanned(it.getPoppedArgument(argumentKey = QR_KEY))

        options(getFamilyMenuState(state))

        FamilyPage(
            state = state,
            onQRScanRequested = onQRScanRequested,
            onBack = onBack,
        )
    }
}

fun getFamilyMenuState(
    state: FamilyState,
): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_ok,
        onMenuClick = state.onSave,
    )
}

data object FamilyRoute : BaseDestination(route = "FamilyRoute") {
    operator fun invoke() = route
}
