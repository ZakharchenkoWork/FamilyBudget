package com.faigenbloom.famillyspandings

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import com.faigenbloom.famillyspandings.ui.spandings.detail.mockSuggestions
import com.faigenbloom.famillyspandings.ui.spandings.list.mockSpendingsWithCategoryList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpendingsCreateTest : BaseTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    override fun getTestRule() = composeTestRule

    @Test
    fun can_create_fully_filled_spending() {
        waitForIdle()
        openNewSpending()
        chooseFirstCategory()
        moveToSpendingInfo()
        fillSpendingNameAndAmount(
            mockSpendingsWithCategoryList[0].name,
            mockSpendingsWithCategoryList[0].amount.toReadableMoney(),
        )
        startAddingDetail()
        fillDetailDialogManual(mockSuggestions[0])
        clickOk()
        clickSaveSpending()
        waitForIdle()
        clickBack()

    }


}
