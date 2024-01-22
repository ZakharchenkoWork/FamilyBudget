package com.faigenbloom.famillyspandings.ui.settings

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import java.util.Currency

class SettingsPageRepository(private val dataSource: BaseDataSource) {
    fun getAllCurrencies(): List<Currency> {
        return dataSource.getAllCurrencies()
    }

    suspend fun getChosenCurrency(): Currency {
        return dataSource.getChosenCurrency()
    }
}
