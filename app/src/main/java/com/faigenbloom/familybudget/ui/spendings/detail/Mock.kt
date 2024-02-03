package com.faigenbloom.familybudget.ui.spendings.detail

import com.faigenbloom.familybudget.ui.spendings.DetailUiData

const val DETAIL_DIALOG_ADD_TO_LIST = "DETAIL_DIALOG_ADD_TO_LIST"
const val DETAIL_DIALOG_NAME_INPUT = "DETAIL_DIALOG_NAME_INPUT"
const val DETAIL_DIALOG_AMOUNT_INPUT = "DETAIL_DIALOG_AMOUNT_INPUT"
const val SUGGESTION_TITLE = "SUGGESTION_TITLE"
const val DETAIL_NAME_TITLE = "DETAIL_NAME_TITLE"
const val OK_BUTTON = "OK_BUTTON"

val mockSuggestions = listOf(
    DetailUiData(
        id = "1",
        name = "Nuts",
        amount = "15.00",
    ),
    DetailUiData(
        id = "2",
        name = "Guts",
        amount = "15.00",
    ),
    DetailUiData(
        id = "3",
        name = "Brut Wine",
        amount = "15.00",
    ),
    DetailUiData(
        id = "4",
        name = "Utils",
        amount = "15.00",
    ),
)
