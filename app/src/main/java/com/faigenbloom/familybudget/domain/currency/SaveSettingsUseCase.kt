package com.faigenbloom.familybudget.domain.currency

import com.faigenbloom.familybudget.datasources.db.entities.SettingsEntity
import com.faigenbloom.familybudget.domain.family.SaveUserUseCase
import com.faigenbloom.familybudget.repositories.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency

class SaveSettingsUseCase(
    private val settingsRepository: SettingsRepository,
    private val saveUserUseCase: SaveUserUseCase,
) {
    suspend operator fun invoke(
        currency: Currency,
        isNotificationsEnabled: Boolean,
        isPasswordEnabled: Boolean,
        name: String,
        familyName: String,
    ) {
        withContext(Dispatchers.IO) {
            settingsRepository.saveSettings(
                SettingsEntity(
                    currency = currency.currencyCode,
                    isNotificationsEnabled = isNotificationsEnabled,
                    isPasswordEnabled = isPasswordEnabled,

                    ),
            )
            saveUserUseCase(name, familyName)
        }
    }
}
