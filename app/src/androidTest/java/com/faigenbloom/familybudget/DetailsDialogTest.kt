package com.faigenbloom.familybudget


import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faigenbloom.familybudget.ui.spendings.detail.DETAIL_DIALOG_ADD_TO_LIST
import com.faigenbloom.familybudget.ui.spendings.detail.DETAIL_DIALOG_NAME_INPUT
import com.faigenbloom.familybudget.ui.spendings.detail.DETAIL_NAME_TITLE
import com.faigenbloom.familybudget.ui.spendings.detail.SUGGESTION_TITLE
import com.faigenbloom.familybudget.ui.spendings.detail.mockSuggestions
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_DETAIL_AMOUNT
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_DETAIL_NAME
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsDialogTest : BaseTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    override fun getTestRule() = composeTestRule

    @Test
    fun suggestions_works_correctly() {
        waitForIdle()
        openNewSpending()
        moveToSpendingInfo()
        startAddingDetail()

        composeTestRule.onNodeWithContentDescription(DETAIL_DIALOG_NAME_INPUT)
            .performTextInput("ut")
        composeTestRule.onNodeWithContentDescription(SUGGESTION_TITLE)
            .assertExists().assert(hasText(mockSuggestions[0].name))
        composeTestRule.onNodeWithContentDescription(SUGGESTION_TITLE).performClick()
        composeTestRule.onNodeWithContentDescription(DETAIL_DIALOG_NAME_INPUT)
            .assert(hasText(mockSuggestions[0].name))

    }

    @Test
    fun suggestions_added_to_list_correctly() {
        waitForIdle()
        openNewSpending()
        moveToSpendingInfo()
        startAddingDetail()
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_NAME_INPUT).performTextInput("ut")
        getTestRule().onNodeWithContentDescription(SUGGESTION_TITLE).performClick()
        getTestRule().onNodeWithContentDescription(DETAIL_DIALOG_ADD_TO_LIST).performClick()
        composeTestRule.onNodeWithContentDescription(DETAIL_NAME_TITLE)
            .assertExists().assert(hasText(mockSuggestions[0].name))
    }

    @Test
    fun details_saved_correctly_in_spending() {
        waitForIdle()
        openNewSpending()
        moveToSpendingInfo()
        startAddingDetail()
        fillDetailDialog(mockSuggestions[0])
        clickOk()
        composeTestRule.onNodeWithContentDescription(SPENDING_DETAIL_NAME + 0)
            .assertExists().assert(hasText(mockSuggestions[0].name))
        composeTestRule.onNodeWithContentDescription(SPENDING_DETAIL_AMOUNT + 0)
            .assertExists().assert(hasText(mockSuggestions[0].amount))
    }

    @Test
    fun details_opened_correctly_from_spending() {
        waitForIdle()
        openNewSpending()
        moveToSpendingInfo()
        startAddingDetail()
        fillDetailDialog(mockSuggestions[0])
        clickOk()
        startAddingDetail()
        composeTestRule.onNodeWithContentDescription(DETAIL_NAME_TITLE)
            .assertExists().assert(hasText(mockSuggestions[0].name))
    }

    @Test
    fun suggestions_saved_correctly_with_manual() {
        waitForIdle()
        openNewSpending()
        moveToSpendingInfo()
        startAddingDetail()
        fillDetailDialogManual(mockSuggestions[0])
        clickOk()

        composeTestRule.onNodeWithContentDescription(SPENDING_DETAIL_NAME + 0)
            .assertExists().assert(hasText(mockSuggestions[0].name))
        composeTestRule.onNodeWithContentDescription(SPENDING_DETAIL_AMOUNT + 0)
            .assertExists().assert(hasText(mockSuggestions[0].amount))
    }

    @Test
    fun suggestions_saved_correctly_with_two_details() {
        waitForIdle()
        openNewSpending()
        moveToSpendingInfo()
        startAddingDetail()
        fillDetailDialogManual(mockSuggestions[0])
        fillDetailDialogManual(mockSuggestions[1])
        clickOk()
        composeTestRule.onNodeWithContentDescription(SPENDING_DETAIL_NAME + 0)
            .assertExists().assert(hasText(mockSuggestions[0].name))
        composeTestRule.onNodeWithContentDescription(SPENDING_DETAIL_AMOUNT + 0)
            .assertExists().assert(hasText(mockSuggestions[0].amount))
        composeTestRule.onNodeWithContentDescription(SPENDING_DETAIL_NAME + 1)
            .assertExists().assert(hasText(mockSuggestions[1].name))
        composeTestRule.onNodeWithContentDescription(SPENDING_DETAIL_AMOUNT + 1)
            .assertExists().assert(hasText(mockSuggestions[1].amount))
    }
}
