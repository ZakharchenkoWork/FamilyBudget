package com.faigenbloom.famillyspandings

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.faigenbloom.famillyspandings.comon.RIGHT_TOP_BAR_BUTTON
import com.faigenbloom.famillyspandings.ui.categories.FIRST_CATEGORY
import com.faigenbloom.famillyspandings.ui.spandings.DetailUiData
import com.faigenbloom.famillyspandings.ui.spandings.detail.DETAIL_DIALOG_ADD_TO_LIST
import com.faigenbloom.famillyspandings.ui.spandings.detail.DETAIL_DIALOG_AMOUNT_INPUT
import com.faigenbloom.famillyspandings.ui.spandings.detail.DETAIL_DIALOG_NAME_INPUT
import com.faigenbloom.famillyspandings.ui.spandings.detail.OK_BUTTON
import com.faigenbloom.famillyspandings.ui.spandings.detail.SUGGESTION_TITLE
import com.faigenbloom.famillyspandings.ui.spandings.edit.ADD_DETAIL_BUTTON
import com.faigenbloom.famillyspandings.ui.spandings.edit.SpendingEditRoute


abstract class BaseTest {

    protected fun waitForIdle() = getTestRule().waitForIdle()
    protected fun openNewSpending() =
        getTestRule().onNodeWithContentDescription(SpendingEditRoute()).performClick()

    protected fun chooseFirstCategory() =
        getTestRule().onNodeWithContentDescription(FIRST_CATEGORY).performClick()

    protected fun moveToSpendingInfo() =
        getTestRule().onNodeWithContentDescription(RIGHT_TOP_BAR_BUTTON).performClick()

    protected fun startAddingDetail() =
        getTestRule().onNodeWithContentDescription(ADD_DETAIL_BUTTON).performClick()

    protected fun fillDetailDialog(
        spendingDetail: DetailUiData,
    ) {
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_NAME_INPUT)
            .performTextInput(spendingDetail.name)
        getTestRule().onNodeWithContentDescription(SUGGESTION_TITLE).performClick()
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_ADD_TO_LIST).performClick()
    }

    protected fun fillDetailDialogManual(
        spendingDetail: DetailUiData,
    ) {
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_NAME_INPUT)
            .performTextInput(spendingDetail.name)
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_AMOUNT_INPUT)
            .performTextInput(spendingDetail.amount)
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_ADD_TO_LIST).performClick()
    }

    protected fun clickOk() {
        getTestRule().onNodeWithContentDescription(OK_BUTTON).performClick()
    }

    protected abstract fun getTestRule(): AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
    protected fun getString(@StringRes id: Int): String {
        return getTestRule().activity.getString(id)
    }

    /* private fun hasBackground(node: SemanticsNode, color: Color, shape: Shape): Boolean {
         return node.layoutInfo.getModifierInfo().filter { modifierInfo ->
             modifierInfo.modifier == Modifier.background(color, shape)
         }.size == 1
     }*/
    fun backgroundColor(
        color: Color,
    ): SemanticsMatcher {
        val propertyName = "backgroundColor"
        return SemanticsMatcher(
            "$propertyName is '$color'",
        ) {
            it.layoutInfo.getModifierInfo().filter { modifierInfo ->
                modifierInfo.modifier == Modifier.background(color = color)
            }.size == 1
        }
    }
}

