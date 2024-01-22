package com.faigenbloom.famillyspandings.domain

import com.faigenbloom.famillyspandings.repositories.CurrencyRepository
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
