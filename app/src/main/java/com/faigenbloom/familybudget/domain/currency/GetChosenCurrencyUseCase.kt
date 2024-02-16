package com.faigenbloom.familybudget.domain.currency

import com.faigenbloom.familybudget.repositories.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency
import java.util.Locale

class GetChosenCurrencyUseCase(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(): Currency {
        return withContext(Dispatchers.IO) {
            val currency = settingsRepository.getSettings()?.currency?.let {
                it.ifEmpty {
                    getDefault()
                }
            } ?: getDefault()

            Currency.getInstance(currency)
        }
    }
}

private fun getDefault() = Currency.getInstance(Locale.getDefault()).currencyCode
