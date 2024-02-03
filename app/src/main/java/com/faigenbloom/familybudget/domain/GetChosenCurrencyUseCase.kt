package com.faigenbloom.familybudget.domain

import com.faigenbloom.familybudget.repositories.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency

class GetChosenCurrencyUseCase(private val currencyRepository: CurrencyRepository) {
    suspend operator fun invoke(): Currency {
        return withContext(Dispatchers.IO) {
            currencyRepository.getChosenCurrency()
        }
    }
}
