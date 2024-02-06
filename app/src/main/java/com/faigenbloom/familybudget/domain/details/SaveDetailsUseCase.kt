package com.faigenbloom.familybudget.domain.details

import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.repositories.DetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveDetailsUseCase<T : Identifiable>(
    private val detailsRepository: DetailsRepository,
    private val detailsMapper: Mapper<T, SpendingDetailEntity>,
) {
    suspend operator fun invoke(
        spendingId: String,
        details: List<T>,
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
