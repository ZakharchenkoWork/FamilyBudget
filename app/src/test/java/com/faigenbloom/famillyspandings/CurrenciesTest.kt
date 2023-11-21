package com.faigenbloom.famillyspandings

import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.util.Currency
import java.util.Locale

class CurrenciesTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `check local currency`() = runTest {
        val currency = Currency.getInstance(Locale.CANADA).symbol

        currency shouldNotBe null
        println(currency)
    }
}
