package com.faigenbloom.familybudget.ui.settings

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseDestination
import com.faigenbloom.familybudget.common.FloatingMenuState
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.settingsPage(
    bottomNavigationOptions: (
        showNavigation: Boolean,
        index: Int,
    ) -> Unit,
    options: (menuState: FloatingMenuState?) -> Unit,
    onFamilyPageClicked: () -> Unit,
) {
    composable(
        route = SettingsRoute(),
    ) {
        bottomNavigationOptions(true, 5)
        val state by koinViewModel<SettingsPageViewModel>().stateFlow.collectAsState()
        options(getSettingsMenuState(state))
        SettingsPage(
            state = state,
            onFamilyPageClicked = onFamilyPageClicked,
        )
    }
}

fun getSettingsMenuState(
    state: SettingsState,
): FloatingMenuState? {
    return if (state.canSave) {
        FloatingMenuState(
            icon = R.drawable.icon_ok,
            onMenuClick = state.onSave,
        )
    } else null
}

data object SettingsRoute : BaseDestination(route = "SettingsRoute") {
    operator fun invoke() = route
}
