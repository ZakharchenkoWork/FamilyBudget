package com.faigenbloom.famillyspandings.comon

sealed class Destination(val route: String) {
    object Onboarding : Destination(route = "Onboarding")
    object Login : Destination(route = "Login")
    object SpendingsPage : Destination(route = "SpendingsPage")
}
