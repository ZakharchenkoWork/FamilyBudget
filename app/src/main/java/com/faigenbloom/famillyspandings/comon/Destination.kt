package com.faigenbloom.famillyspandings.comon

sealed class Destination(val route: String) {
    object Onboarding : Destination(route = "Onboarding")
    object Login : Destination(route = "Login")
    object Register : Destination(route = "Register")
    object SpendingsPage : Destination(route = "SpendingsPage")
    object SettingsPage : Destination(route = "SettingsPage")
    object StatisticsPage : Destination(route = "StatisticsPage")
    object BudgetPage : Destination(route = "BudgetPage")
    object FamilyPage : Destination(route = "FamilyPage")
    object QRScanPage : Destination(route = "QRScanPage")
    object PhotoChooserDialog : Destination(route = "PhotoChooserDialog/$PHOTO_REASON/$ID_KEY") {
        fun withReason(reason: String?, id: String?): String {
            return route
                .replace(PHOTO_REASON, reason ?: PHOTO_REASON)
                .replace(ID_KEY, id ?: ID_KEY)
        }
    }

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

    object Camera : Destination(route = "Camera/$PHOTO_REASON/$ID_KEY") {
        fun withReason(reason: String?, id: String?): String {
            return route
                .replace(PHOTO_REASON, reason ?: PHOTO_REASON)
                .replace(ID_KEY, id ?: ID_KEY)
        }
    }
}

const val PHOTO_REASON_ARG: String = "PHOTO_REASON"
const val PHOTO_REASON: String = "{$PHOTO_REASON_ARG}"
const val PHOTO_KEY: String = "PHOTO_URI"
const val SPENDING_ID_ARG = "spendingId"
const val SPENDING_ID_KEY: String = "{$SPENDING_ID_ARG}"

const val ID_ARG = "ID"
const val ID_KEY: String = "{$ID_ARG}"

const val SPENDING_PHOTO: String = "SPENDING"
const val CATEGORY_PHOTO: String = "CATEGORY"

const val QR_KEY: String = "QR_TEXT"
