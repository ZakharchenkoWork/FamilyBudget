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
    object CalendarDialog : Destination(route = "Calendar/$CALENDAR_START_DATE") {
        fun withDate(startDate: String): String {
            return route
                .replace(
                    CALENDAR_START_DATE,
                    if (startDate.isEmpty()) {
                        CALENDAR_START_DATE
                    } else {
                        startDate
                    },
                )
        }
    }

    object SpendingEditPage : Destination(route = "SpendingEditPage$SPENDING_ID_QUERY") {
        fun withId(id: String): String {
            return route.replace(SPENDING_OPTIONAL_ID_KEY, id)
        }

        fun withoutId(): String {
            return route.replace(ID_KEY, "")
        }
    }

    object NewSpendingPage : Destination(route = "SpendingEditPage")
    object SpendingShowPage : Destination(route = "SpendingShowPage/$ID_KEY") {
        fun withId(id: String): String {
            return route.replace(ID_KEY, id)
        }
    }

    object PhotoChooserDialog :
        Destination(route = "PhotoChooserDialog/$PHOTO_REASON$ID_KEY_QUERY") {
        fun withReason(reason: String?, id: String?): String {
            return route
                .replace(PHOTO_REASON, reason ?: PHOTO_REASON)
                .replace(OPTIONAL_ID_KEY, id ?: "")
        }
    }

    object Camera : Destination(route = "Camera/$PHOTO_REASON$ID_KEY_QUERY") {
        fun withReason(reason: String?, id: String?): String {
            return route
                .replace(PHOTO_REASON, reason ?: PHOTO_REASON)
                .replace(OPTIONAL_ID_KEY, id ?: "")
        }
    }
}

const val PHOTO_REASON_ARG: String = "PHOTO_REASON"
const val PHOTO_REASON: String = "{$PHOTO_REASON_ARG}"
const val PHOTO_KEY: String = "PHOTO_URI"

const val OPTIONAL_ID_ARG = "OPTIONAL_ID"
const val ID_ARG = "ID"
const val OPTIONAL_ID_KEY: String = "{$OPTIONAL_ID_ARG}"
const val ID_KEY: String = "{$ID_ARG}"
const val SPENDING_ID_ARG = "spendingId"
const val SPENDING_OPTIONAL_ID_KEY = "{$SPENDING_ID_ARG}"

const val ID_KEY_QUERY: String = "?$ID_ARG=$OPTIONAL_ID_KEY"
const val SPENDING_ID_QUERY: String = "?$ID_ARG=$SPENDING_OPTIONAL_ID_KEY"

const val CALENDAR_START_DATE_ARG: String = "CALENDAR_START_DATE"
const val CALENDAR_START_DATE: String = "{$CALENDAR_START_DATE_ARG}"

const val SPENDING_PHOTO: String = "SPENDING"
const val CATEGORY_PHOTO: String = "CATEGORY"

const val QR_KEY: String = "QR_TEXT"
const val DATE: String = "DATE"

class DestinationHelper
