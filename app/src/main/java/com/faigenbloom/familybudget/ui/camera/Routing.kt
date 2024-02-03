package com.faigenbloom.familybudget.ui.camera

import android.net.Uri
import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faigenbloom.familybudget.common.BaseDestination
import com.faigenbloom.familybudget.common.ID_KEY_QUERY
import com.faigenbloom.familybudget.common.OPTIONAL_ID_ARG
import com.faigenbloom.familybudget.common.OPTIONAL_ID_KEY
import com.faigenbloom.familybudget.common.PHOTO_REASON
import com.faigenbloom.familybudget.common.PHOTO_REASON_ARG
import java.io.File

fun NavGraphBuilder.cameraPage(
    bottomNavigationOptions: (
        showNavigation: Boolean,
    ) -> Unit,
    outputDirectory: File,
    onImageCaptured: (uri: Uri, photoReason: String?, id: String?) -> Unit,
) {
    composable(
        route = CameraRoute.route,
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
        bottomNavigationOptions(false)
        CameraScreen(
            outputDirectory = outputDirectory,
            onImageCaptured = {
                onImageCaptured(it, reason, id)
            },
            onError = { Log.e("Camera", "View error:", it) },
        )
    }
}

data object CameraRoute : BaseDestination(route = "Camera/$PHOTO_REASON$ID_KEY_QUERY") {
    operator fun invoke(reason: String?, id: String?): String {
        return route
            .replace(PHOTO_REASON, reason ?: PHOTO_REASON)
            .replace(OPTIONAL_ID_KEY, id ?: "")
    }
}
