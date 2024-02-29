package com.faigenbloom.familybudget.domain.currency

import com.faigenbloom.familybudget.domain.family.GetThisPersonUseCase
import com.faigenbloom.familybudget.domain.mappers.SettingsMapper
import com.faigenbloom.familybudget.repositories.SettingsRepository
import com.faigenbloom.familybudget.ui.settings.SettingUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository,
    private val getThisPersonUseCase: GetThisPersonUseCase,
    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,
    private val mapper: SettingsMapper,
) {
    suspend operator fun invoke(): SettingUiData {
        return withContext(Dispatchers.IO) {
            val thisPerson = getThisPersonUseCase()
            settingsRepository.getSettings()?.let {
                mapper.forUI(it, thisPerson)
            } ?: SettingUiData(
                currency = getChosenCurrencyUseCase(),
                isNotificationsEnabled = true,
                isPasswordEnabled = false,
                familyName = thisPerson.familyName,
                name = thisPerson.name,
            )
        }
    }
}
