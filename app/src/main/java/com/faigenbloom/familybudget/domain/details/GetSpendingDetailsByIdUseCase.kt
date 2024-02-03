package com.faigenbloom.familybudget.domain.details

import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.datasources.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.repositories.DetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSpendingDetailsByIdUseCase<T : Identifiable>(
    private val detailsRepository: DetailsRepository,
    private val mapper: Mapper<T, SpendingDetailEntity>,
) {
    suspend operator fun invoke(spendingId: String) =
        withContext(Dispatchers.IO) {
            if (spendingId.isNotBlank()) {
                detailsRepository.getSpendingDetails(spendingId).map { mapper.forUI(it) }
            } else {
                emptyList()
            }
        }
}

