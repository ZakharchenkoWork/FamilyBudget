package com.faigenbloom.famillyspandings.ui.family

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.common.BaseDestination
import com.faigenbloom.famillyspandings.common.FloatingMenuState
import com.faigenbloom.famillyspandings.common.QR_KEY
import com.faigenbloom.famillyspandings.common.getPoppedArgument
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
