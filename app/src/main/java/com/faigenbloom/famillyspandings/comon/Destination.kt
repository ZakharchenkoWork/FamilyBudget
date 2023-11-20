package com.faigenbloom.famillyspandings.comon

sealed class Destination(val route: String) {
    object Onboarding : Destination(route = "Onboarding")
    object Login : Destination(route = "Login")
    object Register : Destination(route = "Register")
    object SpendingsPage : Destination(route = "SpendingsPage")
    object SettingsPage : Destination(route = "SettingsPage")
    object StatisticsPage : Destination(route = "StatisticsPage")
    object BudgetPage : Destination(route = "BudgetPage")

    object SpendingEditPage : Destination(route = "SpendingEditPage/$SPENDING_ID_KEY") {
        fun withId(id: String): String {
            return route.replace(SPENDING_ID_KEY, id)
        }
    }

    object SpendingShowPage : Destination(route = "SpendingShowPage/$SPENDING_ID_KEY") {
        fun withId(id: String): String {
            return route.replace(SPENDING_ID_KEY, id)
        }
    }

    object Camera : Destination(route = "Camera")
}

const val PHOTO_KEY: String = "PHOTO_URI"
const val SPENDING_ID_ARG = "spendingId"
const val SPENDING_ID_KEY: String = "{$SPENDING_ID_ARG}"
