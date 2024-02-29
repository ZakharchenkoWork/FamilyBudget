package com.faigenbloom.familybudget.domain.details

import com.faigenbloom.familybudget.domain.mappers.DetailsMapper
import com.faigenbloom.familybudget.repositories.DetailsRepository

class GetAllSpendingDetailsUseCase(
    private val detailsRepository: DetailsRepository,
    private val mapper: DetailsMapper,
) {
    suspend operator fun invoke() =
        detailsRepository.getAllDetails().map { mapper.forUI(it) }
}

