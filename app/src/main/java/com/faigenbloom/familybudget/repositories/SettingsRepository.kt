package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.db.entities.SettingsEntity
import java.util.Currency

class SettingsRepository(private val dataSource: BaseDataSource) {
    suspend fun saveSettings(settingsEntity: SettingsEntity) {
        dataSource.saveSettings(settingsEntity)
    }

    suspend fun getSettings(): SettingsEntity? {
        return dataSource.getSettings()
    }

    suspend fun getAllCurrencies(): List<Currency> {
        return dataSource.getAllCurrencies()
    }
}
