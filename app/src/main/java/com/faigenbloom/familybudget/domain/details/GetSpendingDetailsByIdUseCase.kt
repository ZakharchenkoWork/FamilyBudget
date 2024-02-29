package com.faigenbloom.familybudget.domain.details

import com.faigenbloom.familybudget.domain.mappers.DetailsMapper
import com.faigenbloom.familybudget.repositories.DetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSpendingDetailsByIdUseCase(
    private val detailsRepository: DetailsRepository,
    private val mapper: DetailsMapper,
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

