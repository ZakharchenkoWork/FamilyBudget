package com.faigenbloom.famillyspandings.comon

sealed class Destination(val route: String) {
    object Onboarding : Destination(route = "Onboarding")
    object Login : Destination(route = "Login")
    object Register : Destination(route = "Register")
    object SpendingsPage : Destination(route = "SpendingsPage")
    object SpendingEditPage : Destination(route = "SpendingEditPage")
    object Camera : Destination(route = "Camera")
}

const val PHOTO_KEY: String = "PHOTO_URI"
