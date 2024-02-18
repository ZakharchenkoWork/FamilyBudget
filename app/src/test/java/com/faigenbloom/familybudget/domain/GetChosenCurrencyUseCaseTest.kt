package com.faigenbloom.familybudget.domain

import android.util.Log
import com.faigenbloom.familybudget.MainDispatcherRule
import com.faigenbloom.familybudget.datasources.MockDataSource
import com.faigenbloom.familybudget.datasources.db.entities.SettingsEntity
import com.faigenbloom.familybudget.domain.currency.GetChosenCurrencyUseCase
import com.faigenbloom.familybudget.repositories.SettingsRepository
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
        wheneverBlocking { it.getSettings() }
            .thenReturn(
                SettingsEntity(
                    currency = Currency.getInstance(Locale.getDefault()).currencyCode,
                    id = 0L,
                    isNotificationsEnabled = false,
                    isPasswordEnabled = false,
                ),
            )
    }
    val getChosenCurrencyUseCase: GetChosenCurrencyUseCase =
        GetChosenCurrencyUseCase(SettingsRepository(dataSource))

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
        verify(dataSource).getSettings()
    }
}
