package com.faigenbloom.famillyspandings.ui.settings

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.famillyspandings.comon.BaseDestination

import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.settingsPage(
    bottomNavigationOptions: (
        showNavigation: Boolean,
        index: Int,
    ) -> Unit,
    onFamilyPageClicked: () -> Unit,
) {
    composable(
        route = SettingsRoute(),
    ) {
        bottomNavigationOptions(true, 5)
        val state by koinViewModel<SettingsPageViewModel>().stateFlow.collectAsState()

        SettingsPage(
            state = state,
            onFamilyPageClicked = onFamilyPageClicked,
        )
    }
}

data object SettingsRoute : BaseDestination(route = "SettingsRoute") {
    operator fun invoke() = route
}
