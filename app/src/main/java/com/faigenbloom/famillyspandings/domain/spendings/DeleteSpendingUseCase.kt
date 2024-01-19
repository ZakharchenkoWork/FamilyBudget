package com.faigenbloom.famillyspandings.domain.spendings

import com.faigenbloom.famillyspandings.repositories.DetailsRepository
import com.faigenbloom.famillyspandings.repositories.SpendingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteSpendingUseCase(
    private val spendingsRepository: SpendingsRepository,
    private val detailsRepository: DetailsRepository,
) {
    suspend operator fun invoke(spendingId: String) {
        withContext(Dispatchers.IO) {
            if (spendingId.isNotBlank()) {
                val spendingDetails = detailsRepository.getSpendingDetails(spendingId)
                spendingDetails.forEach { detail ->
                    val detailCrossRefs = detailsRepository.getDetailCrossRefs(detail.id)
                    if (detailCrossRefs.size == 1) {
                        detailsRepository.deleteSpendingDetail(detail.id)
                    }
                    detailsRepository.deleteCrossRef(spendingId, detail.id)
                }
                spendingsRepository.deleteSpending(spendingId)
            }
        }
    }
}
