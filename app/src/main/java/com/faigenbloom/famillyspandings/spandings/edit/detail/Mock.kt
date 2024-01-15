package com.faigenbloom.famillyspandings.spandings.edit.detail

import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail

const val DETAIL_DIALOG_ADD_TO_LIST = "DETAIL_DIALOG_ADD_TO_LIST"
const val DETAIL_DIALOG_NAME_INPUT = "DETAIL_DIALOG_NAME_INPUT"
const val DETAIL_DIALOG_AMOUNT_INPUT = "DETAIL_DIALOG_AMOUNT_INPUT"
const val SUGGESTION_TITLE = "SUGGESTION_TITLE"
const val DETAIL_NAME_TITLE = "DETAIL_NAME_TITLE"
const val OK_BUTTON = "OK_BUTTON"

val mockSuggestions = listOf(
    SpendingDetail(
        id = "1",
        name = "Nuts",
        amount = "15.00",
    ),
    SpendingDetail(
        id = "2",
        name = "Guts",
        amount = "15.00",
    ),
    SpendingDetail(
        id = "3",
        name = "Brut Wine",
        amount = "15.00",
    ),
    SpendingDetail(
        id = "4",
        name = "Utils",
        amount = "15.00",
    ),
)
