package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.domain.mappers.RepeatableOptionsMapper
import com.faigenbloom.familybudget.domain.mappers.SpendingsMapper
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSpendingUseCase(
    private val spendingsRepository: SpendingsRepository,
    private val mapper: SpendingsMapper,
    private val optionsMapper: RepeatableOptionsMapper,
) {
    suspend operator fun invoke(id: String): SpendingUiData {
        return withContext(Dispatchers.IO) {
            val entity = spendingsRepository.getSpending(id)
            var spendingForUI = mapper.forUI(entity)

            if (entity.repeatOptionsId.isNotBlank()){
                val repeatableOption = spendingsRepository.getRepeatableOption(entity.repeatOptionsId)
                spendingForUI = spendingForUI.copy(repeatOptions = optionsMapper.forUI(repeatableOption))
            }
            spendingForUI
        }
    }
}
