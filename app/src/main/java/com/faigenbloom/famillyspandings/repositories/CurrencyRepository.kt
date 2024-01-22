package com.faigenbloom.famillyspandings.repositories

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import java.util.Currency

class CurrencyRepository(private val dataSource: BaseDataSource) {
    suspend fun getChosenCurrency(): Currency {
        return dataSource.getChosenCurrency()
    }
}
