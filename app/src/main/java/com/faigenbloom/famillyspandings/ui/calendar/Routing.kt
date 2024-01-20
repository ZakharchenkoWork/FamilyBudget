package com.faigenbloom.famillyspandings.ui.calendar

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.faigenbloom.famillyspandings.comon.BaseDestination
import com.faigenbloom.famillyspandings.comon.CALENDAR_END_DATE
import com.faigenbloom.famillyspandings.comon.CALENDAR_END_DATE_ARG
import com.faigenbloom.famillyspandings.comon.CALENDAR_START_DATE
import com.faigenbloom.famillyspandings.comon.CALENDAR_START_DATE_ARG

fun NavGraphBuilder.calendarDialog(
    onDatePicked: (String, String) -> Unit,
) {
    dialog(
        route = CalendarRoute.route,
        dialogProperties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
        arguments = listOf(
            navArgument(CALENDAR_START_DATE_ARG) {
                type = NavType.StringType
            },
            navArgument(CALENDAR_END_DATE_ARG) {
                type = NavType.StringType
            },
        ),
    ) { backStackEntry ->

        val startDate =
            backStackEntry.arguments?.getString(CALENDAR_START_DATE_ARG)
                ?: ""
        val endDate =
            backStackEntry.arguments?.getString(CALENDAR_END_DATE_ARG)?.let {
                if (it == CALENDAR_END_DATE) {
                    ""
                } else {
                    it
                }
            } ?: ""


        Calendar(
            startDate = startDate,
            endDate = endDate,
            onDatePicked = onDatePicked,
        )
    }
}

data object CalendarRoute :
    BaseDestination(route = "Calendar/$CALENDAR_START_DATE/$CALENDAR_END_DATE") {
    operator fun invoke(startDate: String, endDate: String = ""): String {
        return route
            .replace(
                CALENDAR_START_DATE,
                startDate.ifEmpty {
                    CALENDAR_START_DATE
                },
            ).replace(
                CALENDAR_END_DATE,
                endDate.ifEmpty {
                    CALENDAR_END_DATE
                },
            )
    }
}
