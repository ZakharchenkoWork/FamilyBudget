package com.faigenbloom.familybudget.domain.mappers

import com.faigenbloom.familybudget.datasources.db.entities.SettingsEntity
import com.faigenbloom.familybudget.ui.family.PersonUiData
import com.faigenbloom.familybudget.ui.settings.SettingUiData
import java.util.Currency

class SettingsMapper {
    fun forUI(entity: SettingsEntity, person: PersonUiData): SettingUiData {
        return SettingUiData(
            currency = Currency.getInstance(entity.currency),
            isNotificationsEnabled = entity.isNotificationsEnabled,
            isPasswordEnabled = entity.isPasswordEnabled,
            familyName = person.familyName,
            name = person.name,
        )
    }

    fun forDB(model: SettingUiData): SettingsEntity {
        return SettingsEntity(
            currency = model.currency.currencyCode,
            isNotificationsEnabled = model.isNotificationsEnabled,
            isPasswordEnabled = model.isPasswordEnabled,
        )
    }
}
