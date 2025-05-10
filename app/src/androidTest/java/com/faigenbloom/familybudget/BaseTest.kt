package com.faigenbloom.familybudget

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.faigenbloom.familybudget.common.BACK_BUTTON
import com.faigenbloom.familybudget.common.MENU_FLOATING_BUTTON
import com.faigenbloom.familybudget.common.RIGHT_TOP_BAR_BUTTON
import com.faigenbloom.familybudget.datasources.firebase.BASE_URL
import com.faigenbloom.familybudget.ui.categories.FIRST_CATEGORY
import com.faigenbloom.familybudget.ui.spendings.DetailUiData
import com.faigenbloom.familybudget.ui.spendings.detail.DETAIL_DIALOG_ADD_TO_LIST
import com.faigenbloom.familybudget.ui.spendings.detail.DETAIL_DIALOG_AMOUNT_INPUT
import com.faigenbloom.familybudget.ui.spendings.detail.DETAIL_DIALOG_NAME_INPUT
import com.faigenbloom.familybudget.ui.spendings.detail.LOGIN_BUTTON
import com.faigenbloom.familybudget.ui.spendings.detail.OK_BUTTON
import com.faigenbloom.familybudget.ui.spendings.detail.SUGGESTION_TITLE
import com.faigenbloom.familybudget.ui.spendings.edit.ADD_DETAIL_BUTTON
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_AMOUNT_INPUT
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_DAILY_OPTION
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_DATE_INPUT
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_NAME_INPUT
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_REPEATABLE_OPTIONS
import com.faigenbloom.familybudget.ui.spendings.edit.SPENDING_SAVE_BUTTON
import com.faigenbloom.familybudget.ui.spendings.edit.SpendingEditRoute
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


abstract class BaseTest {
    val client = HttpClient(OkHttp) {
        install(DefaultRequest) {
            url {
                takeFrom(BASE_URL)
            }
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("KtorClient", message)
                }
            }
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                },
            )
        }
    }

    protected fun erase() {
        GlobalScope.launch {
            client.get("/erase/cc1af424-dccf-418f-846d-56e4e457736e1746894366808") {

            }
        }
    }

    protected fun waitForIdle() = getTestRule().waitForIdle()

    protected fun startUp() {
        waitForIdle()
        try {
            getTestRule().onNodeWithContentDescription(LOGIN_BUTTON).assertDoesNotExist()
        } catch (assertionError: AssertionError) {
            getTestRule().onNodeWithContentDescription(LOGIN_BUTTON).performClick()
            Thread.sleep(1000L)
            getTestRule().onNodeWithContentDescription(LOGIN_BUTTON).performClick()
            Thread.sleep(3000L)
            waitForIdle()
        }
    }

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

    protected fun setupDate(day: Int) {

        getTestRule().onNodeWithContentDescription(SPENDING_DATE_INPUT).performClick()
        getTestRule().onNodeWithText("$day").performClick()
    }
    protected fun clickRepeatableOption() {
        clickMenuButton()
        getTestRule().onNodeWithContentDescription(SPENDING_REPEATABLE_OPTIONS).performClick()
    }
    protected fun clickRepeatableDaily() {
        getTestRule().onNodeWithContentDescription(SPENDING_DAILY_OPTION).performClick()
    }
    protected fun clickSaveSpending() {
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

