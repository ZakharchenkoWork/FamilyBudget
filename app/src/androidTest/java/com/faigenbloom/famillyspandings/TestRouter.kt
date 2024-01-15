package com.faigenbloom.famillyspandings

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.faigenbloom.famillyspandings.comon.Destination
import com.faigenbloom.famillyspandings.comon.RIGHT_TOP_BAR_BUTTON
import com.faigenbloom.famillyspandings.spandings.edit.ADD_DETAIL_BUTTON
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail
import com.faigenbloom.famillyspandings.spandings.edit.detail.DETAIL_DIALOG_ADD_TO_LIST
import com.faigenbloom.famillyspandings.spandings.edit.detail.DETAIL_DIALOG_AMOUNT_INPUT
import com.faigenbloom.famillyspandings.spandings.edit.detail.DETAIL_DIALOG_NAME_INPUT
import com.faigenbloom.famillyspandings.spandings.edit.detail.OK_BUTTON
import com.faigenbloom.famillyspandings.spandings.edit.detail.SUGGESTION_TITLE


abstract class BaseTest {

    fun waitForIdle() = getTestRule().waitForIdle()
    fun createNewSpending() =
        getTestRule().onNodeWithContentDescription(Destination.NewSpendingPage.route).performClick()

    fun moveToSpendingInfo() =
        getTestRule().onNodeWithContentDescription(RIGHT_TOP_BAR_BUTTON).performClick()

    fun startAddingDetail() =
        getTestRule().onNodeWithContentDescription(ADD_DETAIL_BUTTON).performClick()

    fun fillDetailDialog(
        spendingDetail: SpendingDetail,
    ) {
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_NAME_INPUT)
            .performTextInput(spendingDetail.name)
        getTestRule().onNodeWithContentDescription(SUGGESTION_TITLE).performClick()
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_ADD_TO_LIST).performClick()
    }

    fun fillDetailDialogManual(
        spendingDetail: SpendingDetail,
    ) {
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_NAME_INPUT)
            .performTextInput(spendingDetail.name)
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_AMOUNT_INPUT)
            .performTextInput(spendingDetail.amount)
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_ADD_TO_LIST).performClick()
    }

    fun clickOk() {
        getTestRule().onNodeWithContentDescription(OK_BUTTON).performClick()
    }

    abstract fun getTestRule(): AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
}

