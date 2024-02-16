package com.faigenbloom.familybudget.domain.currency

import com.faigenbloom.familybudget.domain.family.GetThisPersonUseCase
import com.faigenbloom.familybudget.repositories.SettingsRepository
import com.faigenbloom.familybudget.ui.settings.SettingUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository,
    private val getThisPersonUseCase: GetThisPersonUseCase,
    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,
) {
    suspend operator fun invoke(): SettingUiData {
        return withContext(Dispatchers.IO) {
            val thisPerson = getThisPersonUseCase()
            settingsRepository.getSettings()?.let {
                SettingUiData(
                    currency = Currency.getInstance(it.currency),
                    isNotificationsEnabled = it.isNotificationsEnabled,
                    isPasswordEnabled = it.isPasswordEnabled,
                    familyName = thisPerson.familyName,
                    name = thisPerson.name,

                    )
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
