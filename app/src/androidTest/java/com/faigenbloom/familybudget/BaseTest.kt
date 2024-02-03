package com.faigenbloom.familybudget

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
import com.faigenbloom.familybudget.common.BACK_BUTTON
import com.faigenbloom.familybudget.common.MENU_FLOATING_BUTTON
import com.faigenbloom.familybudget.common.RIGHT_TOP_BAR_BUTTON
import com.faigenbloom.familybudget.ui.categories.FIRST_CATEGORY
import com.faigenbloom.familybudget.ui.spendings.DetailUiData
import com.faigenbloom.familybudget.ui.spendings.detail.DETAIL_DIALOG_ADD_TO_LIST
import com.faigenbloom.familybudget.ui.spendings.detail.DETAIL_DIALOG_AMOUNT_INPUT
import com.faigenbloom.familybudget.ui.spendings.detail.DETAIL_DIALOG_NAME_INPUT
import com.faigenbloom.familybudget.ui.spendings.detail.OK_BUTTON
import com.faigenbloom.familybudget.ui.spendings.detail.SUGGESTION_TITLE
import com.faigenbloom.familybudget.ui.spendings.edit.ADD_DETAIL_BUTTON
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_AMOUNT_INPUT
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_NAME_INPUT
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_SAVE_BUTTON
import com.faigenbloom.familybudget.ui.spendings.edit.SpendingEditRoute


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

    protected fun fillSpendingNameAndAmount(
        name: String, amount: String,
    ) {
        getTestRule().onNodeWithContentDescription(SPENDING_NAME_INPUT)
            .performTextInput(name)
        getTestRule().onNodeWithContentDescription(SPENDING_AMOUNT_INPUT)
            .performTextInput(amount)
    }

    protected fun clickSaveSpending() {
        clickMenuButton()
        getTestRule().onNodeWithContentDescription(SPENDING_SAVE_BUTTON).performClick()
    }

    protected fun clickMenuButton() =
        getTestRule().onNodeWithContentDescription(MENU_FLOATING_BUTTON).performClick()

    protected fun clickOk() {
        getTestRule().onNodeWithContentDescription(OK_BUTTON).performClick()
    }

    protected fun clickBack() {
        getTestRule().onNodeWithContentDescription(BACK_BUTTON).performClick()
    }

    protected abstract fun getTestRule(): AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

    protected fun getString(@StringRes id: Int): String {
        return getTestRule().activity.getString(id)
    }

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

