package com.faigenbloom.familybudget.domain.details

import com.faigenbloom.familybudget.domain.mappers.DetailsMapper
import com.faigenbloom.familybudget.repositories.DetailsRepository
import com.faigenbloom.familybudget.ui.spendings.DetailUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveDetailsUseCase(
    private val detailsRepository: DetailsRepository,
    private val detailsMapper: DetailsMapper,
) {
    suspend operator fun invoke(
        spendingId: String,
        details: List<DetailUiData>,
        isDuplicate: Boolean = false,
    ) {
        withContext(Dispatchers.IO) {
            val mappedDetails = details.map { detailsMapper.forDB(it) }
            if (isDuplicate) {
                detailsRepository.saveSpendingDetailsForDuplicate(
                    spendingId, mappedDetails,
                )
            } else {
                detailsRepository.saveSpendingDetails(spendingId, mappedDetails)
            }
        }
    }
}
