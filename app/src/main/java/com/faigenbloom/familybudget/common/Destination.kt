package com.faigenbloom.familybudget.common

open class BaseDestination(
    val route: String,
)

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

const val CALENDAR_END_DATE_ARG: String = "CALENDAR_END_DATE"
const val CALENDAR_END_DATE: String = "{$CALENDAR_END_DATE_ARG}"

const val SPENDING_PHOTO: String = "SPENDING"
const val CATEGORY_PHOTO: String = "CATEGORY"

const val QR_KEY: String = "QR_TEXT"
const val DATE: String = "DATE"

const val DETAILS_LIST_ARG = "DETAILS_LIST"
const val DETAILS_LIST_KEY: String = "{$DETAILS_LIST_ARG}"
