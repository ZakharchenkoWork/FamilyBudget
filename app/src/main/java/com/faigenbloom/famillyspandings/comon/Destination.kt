package com.faigenbloom.famillyspandings.comon

import androidx.lifecycle.SavedStateHandle
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetailListWrapper

sealed class Destination(val route: String) {
    data object Onboarding : Destination(route = "Onboarding")
    data object Login : Destination(route = "Login")
    data object Register : Destination(route = "Register")
    data object SpendingsPage : Destination(route = "SpendingsPage")
    data object SettingsPage : Destination(route = "SettingsPage")
    data object StatisticsPage : Destination(route = "StatisticsPage")
    data object BudgetPage : Destination(route = "BudgetPage")
    data object FamilyPage : Destination(route = "FamilyPage")
    data object CalendarDialog : Destination(route = "Calendar/$CALENDAR_START_DATE") {
        fun withDate(startDate: String): String {
            return route
                .replace(
                    CALENDAR_START_DATE,
                    startDate.ifEmpty {
                        CALENDAR_START_DATE
                    },
                )
        }
    }

    data object DetailDialog : Destination(route = "DetailDialog/$DETAILS_LIST_KEY") {
        fun withDetailsList(detailsList: List<SpendingDetail>): String {
            return route.replace(
                DETAILS_LIST_KEY,
                SpendingDetailListWrapper(ArrayList(detailsList)).toJson(),
            )
        }
    }

    data object SpendingEditPage : Destination(route = "SpendingEditPage$SPENDING_ID_QUERY") {
        fun withId(id: String): String {
            return route.replace(SPENDING_OPTIONAL_ID_KEY, id)
        }

        fun withoutId(): String {
            return route.replace(ID_KEY, "")
        }
    }

    data object NewSpendingPage : Destination(route = "SpendingEditPage")
    data object SpendingShowPage : Destination(route = "SpendingShowPage/$ID_KEY") {
        fun withId(id: String): String {
            return route.replace(ID_KEY, id)
        }
    }

    data object PhotoChooserDialog :
        Destination(route = "PhotoChooserDialog/$PHOTO_REASON$ID_KEY_QUERY") {
        fun withReason(reason: String?, id: String?): String {
            return route
                .replace(PHOTO_REASON, reason ?: PHOTO_REASON)
                .replace(OPTIONAL_ID_KEY, id ?: "")
        }
    }

    data object Camera : Destination(route = "Camera/$PHOTO_REASON$ID_KEY_QUERY") {
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

const val DETAIL_ID: String = "DATE"
const val FULL_DETAIL: String = "FULL_DETAIL"

const val DETAILS_LIST_ARG = "DETAILS_LIST"
const val DETAILS_LIST_KEY: String = "{$DETAILS_LIST_ARG}"

internal class DetailsDialogArgs(val rawData: String? = null) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(savedStateHandle[DETAILS_LIST_ARG])

    fun getList(): ArrayList<SpendingDetail> {
        return rawData?.fromJson<SpendingDetailListWrapper>()?.details ?: arrayListOf()
    }
}
