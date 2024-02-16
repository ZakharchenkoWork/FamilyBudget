package com.faigenbloom.familybudget.domain.currency

import com.faigenbloom.familybudget.repositories.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency
import java.util.Locale

class GetAllCurrenciesUseCase(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(chosenCurrency: Currency): List<Currency> {
        return withContext(Dispatchers.IO) {
            var arrayList = settingsRepository.getAllCurrencies()
            arrayList = ArrayList(
                arrayList.sortedBy {
                    it.currencyCode
                },
            )
            val localCurrency = Currency.getInstance(Locale.getDefault())

            val usd = Currency.getInstance(Locale.US)

            val eur = Currency.getInstance(Locale.FRANCE)
            if (eur != chosenCurrency && eur != localCurrency) {
                arrayList.add(0, arrayList.removeAt(arrayList.indexOf(eur)))
            }
            if (usd != chosenCurrency && usd != localCurrency) {
                arrayList.add(0, arrayList.removeAt(arrayList.indexOf(usd)))
            }
            if (localCurrency != chosenCurrency) {
                arrayList.add(0, arrayList.removeAt(arrayList.indexOf(localCurrency)))
            }
            arrayList.add(0, arrayList.removeAt(arrayList.indexOf(chosenCurrency)))
            arrayList
        }
    }
}
