package com.faigenbloom.familybudget.ui.family

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseDestination
import com.faigenbloom.familybudget.common.FloatingMenuState
import com.faigenbloom.familybudget.common.ID_KEY_QUERY
import com.faigenbloom.familybudget.common.MenuItemState
import com.faigenbloom.familybudget.common.OPTIONAL_ID_ARG
import com.faigenbloom.familybudget.common.OPTIONAL_ID_KEY
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.familyPage(
    bottomNavigationOptions: (
        showNavigation: Boolean,
    ) -> Unit,
    options: (menuState: FloatingMenuState) -> Unit,
    onLinkShareRequest: (String) -> Unit,
    onBack: () -> Unit,
) {
    composable(
        route = FamilyRoute.route,
        arguments = listOf(
            navArgument(OPTIONAL_ID_ARG) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
    ) { backStackEntry ->
        bottomNavigationOptions(false)
        val viewModel = koinViewModel<FamilyPageViewModel>()
        val qrLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
            if (result is QRResult.QRSuccess) {
                result.content.rawValue?.let {
                    viewModel.onNewFamilyIdReceived(it)
                }
            }
        }
        viewModel.onLinkShareRequest = onLinkShareRequest
        val state by viewModel.stateFlow.collectAsState()

        options(
            getFamilyMenuState(
                state,
                onQRScanRequested = {
                    qrLauncher.launch(null)
                },
            ),
        )

        FamilyPage(
            state = state,
            onBack = onBack,
        )
    }
}

fun getFamilyMenuState(
    state: FamilyState,
    onQRScanRequested: () -> Unit,
): FloatingMenuState {
    return FloatingMenuState(
        icon = R.drawable.icon_options,
        alwaysVisibleButton = MenuItemState(
            icon = R.drawable.icon_ok,
            label = R.string.button_save,
            onClick = state.onSave,
        ),
        items = listOf(
            MenuItemState(
                isShown = state.canLeaveFamily,
                icon = R.drawable.icon_exit,
                label = R.string.family_leave,
                onClick = state.onLeaveFamily,
            ),
            MenuItemState(
                icon = R.drawable.icon_share,
                label = R.string.share,
                onClick = state.onShareLink,
            ),
            MenuItemState(
                icon = R.drawable.icon_qrcode,
                label = R.string.qr_code_gen,
                onClick = { state.onQRVisibilityChanged(true) },
            ),
            MenuItemState(
                icon = R.drawable.icon_qrcode_scan,
                label = R.string.qr_code_scan,
                onClick = { onQRScanRequested() },
            ),
        ),
    )
}

data object FamilyRoute : BaseDestination(route = "FamilyRoute/$ID_KEY_QUERY") {
    operator fun invoke(id: String? = null): String {
        return route.replace(OPTIONAL_ID_KEY, id ?: "")
    }
}
