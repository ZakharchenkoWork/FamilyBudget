package com.faigenbloom.familybudget

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.ui.spendings.detail.mockSuggestions
import com.faigenbloom.familybudget.ui.spendings.list.mockSpendingsWithCategoryList
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpendingsRepeatableTest : BaseTest() {

    @Before
    fun setUp() {
        erase()
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    override fun getTestRule() = composeTestRule

    @Test
    fun can_create_repeatable_spending() {

        startUp()
        try {
            openNewSpending()
        } catch (e: AssertionError) {
            waitForIdle()
            openNewSpending()
        }


        fillSpendingNameAndAmount(
            mockSpendingsWithCategoryList[0].name,
            mockSpendingsWithCategoryList[0].amount.toReadableMoney(),
        )
        setupDate(1)
        clickRepeatableOption()
        clickRepeatableDaily()
        moveToSpendingInfo()
        chooseFirstCategory()
        clickSaveSpending()
        waitForIdle()
        clickBack()

    }


}
