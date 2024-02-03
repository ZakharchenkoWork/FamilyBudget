package com.faigenbloom.familybudget.ui.chooser

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.faigenbloom.familybudget.common.BaseDestination
import com.faigenbloom.familybudget.common.ID_KEY_QUERY
import com.faigenbloom.familybudget.common.OPTIONAL_ID_ARG
import com.faigenbloom.familybudget.common.OPTIONAL_ID_KEY
import com.faigenbloom.familybudget.common.PHOTO_REASON
import com.faigenbloom.familybudget.common.PHOTO_REASON_ARG

fun NavGraphBuilder.imageSourceChooserDialog(
    onDismissRequest: () -> Unit,
    onGalleryChosen: (reason: String?, id: String?) -> Unit,
    onCameraChosen: (reason: String?, id: String?) -> Unit,
) {
    dialog(
        route = ImageSourceChooserRoute.route,
        dialogProperties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
        arguments = listOf(
            navArgument(PHOTO_REASON_ARG) {
                type = NavType.StringType
            },
            navArgument(OPTIONAL_ID_ARG) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
    ) { backStackEntry ->
        val reason = backStackEntry.arguments?.getString(PHOTO_REASON_ARG)
        val id = backStackEntry.arguments?.getString(OPTIONAL_ID_ARG)

        ImageSourceChooser(
            onDismissRequest = onDismissRequest,
            onGalleryChosen = { onGalleryChosen(reason, id) },
            onCameraChosen = { onCameraChosen(reason, id) },
        )
    }
}

data object ImageSourceChooserRoute
    : BaseDestination(route = "ImageSourceChooserDialog/$PHOTO_REASON$ID_KEY_QUERY") {
    operator fun invoke(reason: String?, id: String?): String {
        return route
            .replace(PHOTO_REASON, reason ?: PHOTO_REASON)
            .replace(OPTIONAL_ID_KEY, id ?: "")
    }
}
