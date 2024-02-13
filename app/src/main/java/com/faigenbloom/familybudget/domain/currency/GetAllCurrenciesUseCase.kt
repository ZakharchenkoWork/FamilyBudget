package com.faigenbloom.familybudget.domain.currency

import com.faigenbloom.familybudget.repositories.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency

class GetAllCurrenciesUseCase(private val currencyRepository: CurrencyRepository) {
    suspend operator fun invoke(): List<Currency> {
        return withContext(Dispatchers.IO) {
            currencyRepository.getAllCurrencies()
        }
    }
}
