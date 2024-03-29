package com.faigenbloom.familybudget


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faigenbloom.familybudget.datasources.db.entities.DefaultCategories
import com.faigenbloom.familybudget.ui.categories.EDIT_CATEGORY_BUTTON
import com.faigenbloom.familybudget.ui.categories.FIRST_CATEGORY
import com.faigenbloom.familybudget.ui.categories.NEW_CATEGORY_NAME_INPUT
import com.faigenbloom.familybudget.ui.categories.NEW_CATEGORY_SAVE_BUTTON
import com.faigenbloom.familybudget.ui.categories.mockCategoriesList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesTest : BaseTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    override fun getTestRule() = composeTestRule

    @Test
    fun categories_shown_correctly() {
        waitForIdle()
        openNewSpending()
        composeTestRule.onNodeWithContentDescription(FIRST_CATEGORY)
            .assert(hasText(getString(DefaultCategories.getOrNull(mockCategoriesList[0].id)?.nameId!!)))
        composeTestRule.onNodeWithContentDescription(FIRST_CATEGORY)
            .assert(backgroundColor(Color(0xFFFFFFFF)))
        chooseFirstCategory()
        composeTestRule.onNodeWithContentDescription(FIRST_CATEGORY)
            .assert(backgroundColor(Color(0xFFE2CD96)))

    }

    @Test
    fun can_create_new_category() {
        waitForIdle()
        openNewSpending()
        clickMenuButton()

        composeTestRule.onNodeWithContentDescription(NEW_CATEGORY_SAVE_BUTTON)
            .assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription(NEW_CATEGORY_NAME_INPUT)
            .performTextInput("Music")

        composeTestRule.onNodeWithContentDescription(NEW_CATEGORY_SAVE_BUTTON)
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithContentDescription(FIRST_CATEGORY)
            .assert(hasText("Music"))
            .assert(backgroundColor(Color(0xFFE2CD96)))
        composeTestRule.onNodeWithContentDescription(NEW_CATEGORY_SAVE_BUTTON)
            .assertDoesNotExist()
    }

    @Test
    fun can_edit_category() {
        waitForIdle()
        openNewSpending()
        clickMenuButton()
        composeTestRule.onNodeWithContentDescription(EDIT_CATEGORY_BUTTON)
            .assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription(NEW_CATEGORY_NAME_INPUT)
            .performTextInput("Music")

        composeTestRule.onNodeWithContentDescription(NEW_CATEGORY_SAVE_BUTTON)
            .performClick()

        composeTestRule.onNodeWithContentDescription(FIRST_CATEGORY)
            .performClick()
        composeTestRule.onNodeWithContentDescription(EDIT_CATEGORY_BUTTON)
            .assertExists()
            .performClick()
        composeTestRule.onNodeWithContentDescription(NEW_CATEGORY_SAVE_BUTTON)
            .assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription(NEW_CATEGORY_NAME_INPUT)
            .assertExists()
            .performTextInput("Melody")

        composeTestRule.onNodeWithContentDescription(NEW_CATEGORY_SAVE_BUTTON)
            .assertExists()
            .performClick()
        composeTestRule.onNodeWithContentDescription(FIRST_CATEGORY)
            .assert(hasText("MusicMelody"))
            .assert(backgroundColor(Color(0xFFE2CD96)))

    }
}
