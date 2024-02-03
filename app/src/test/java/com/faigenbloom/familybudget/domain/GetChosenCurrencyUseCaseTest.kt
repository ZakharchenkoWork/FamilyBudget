package com.faigenbloom.familybudget.domain

import android.util.Log
import com.faigenbloom.familybudget.MainDispatcherRule
import com.faigenbloom.familybudget.datasources.MockDataSource
import com.faigenbloom.familybudget.repositories.CurrencyRepository
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.wheneverBlocking
import java.util.Currency
import java.util.Locale

class GetChosenCurrencyUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dataSource: MockDataSource = mock {
        wheneverBlocking { it.getChosenCurrency() }
            .thenReturn(Currency.getInstance(Locale.getDefault()))
    }
    val getChosenCurrencyUseCase: GetChosenCurrencyUseCase =
        GetChosenCurrencyUseCase(CurrencyRepository(dataSource))

    @Before
    fun setUp() {
        mockkStatic(Log::class)

        every { Log.e(any(), any()) } answers {
            println(arg<String>(1))
            0
        }
        every { Log.d(any(), any()) } answers {
            println(arg<String>(1))
            0
        }
    }

    @Test
    fun `Can retrieve currency is correct`() = runTest {
        getChosenCurrencyUseCase()
        verify(dataSource).getChosenCurrency()
    }
}
