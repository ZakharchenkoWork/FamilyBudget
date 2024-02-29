package com.faigenbloom.familybudget.domain.currency

import com.faigenbloom.familybudget.domain.family.SaveUserUseCase
import com.faigenbloom.familybudget.domain.mappers.SettingsMapper
import com.faigenbloom.familybudget.repositories.SettingsRepository
import com.faigenbloom.familybudget.ui.settings.SettingUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency

class SaveSettingsUseCase(
    private val settingsRepository: SettingsRepository,
    private val saveUserUseCase: SaveUserUseCase,
    private val mapper: SettingsMapper,
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
                mapper.forDB(
                    SettingUiData(
                        currency = currency,
                        isNotificationsEnabled = isNotificationsEnabled,
                        isPasswordEnabled = isPasswordEnabled,
                        name = "",
                        familyName = "",
                    ),
                ),
            )
            saveUserUseCase(name, familyName)
        }
    }
}
