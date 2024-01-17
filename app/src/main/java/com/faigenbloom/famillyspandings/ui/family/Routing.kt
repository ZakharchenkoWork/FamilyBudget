package com.faigenbloom.famillyspandings.ui.family

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.famillyspandings.comon.BaseDestination
import com.faigenbloom.famillyspandings.comon.QR_KEY
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.familyPage(
    bottomNavigationOptions: (
        showNavigation: Boolean,
    ) -> Unit,
    onQRScanRequested: () -> Unit,
    onBack: () -> Unit,
) {

    composable(
        route = FamilyRoute(),
    ) {
        bottomNavigationOptions(false)

        val state by koinViewModel<FamilyPageViewModel>().familyStateFlow.collectAsState()
        val qrCodeScanned = it.savedStateHandle.get<String>(QR_KEY)
        state.onQrScanned(qrCodeScanned)
        FamilyPage(
            state = state,
            onQRScanRequested = onQRScanRequested,
            onBack = onBack,
        )
    }
}

data object FamilyRoute : BaseDestination(route = "FamilyRoute") {
    operator fun invoke() = route
}
