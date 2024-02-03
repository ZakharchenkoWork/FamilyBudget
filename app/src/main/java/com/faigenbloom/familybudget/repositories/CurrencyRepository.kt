package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import java.util.Currency

class CurrencyRepository(private val dataSource: BaseDataSource) {
    suspend fun getChosenCurrency(): Currency {
        return dataSource.getChosenCurrency()
    }
}
