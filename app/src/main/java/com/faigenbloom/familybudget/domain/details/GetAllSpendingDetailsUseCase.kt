package com.faigenbloom.familybudget.domain.details

import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.datasources.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.repositories.DetailsRepository

class GetAllSpendingDetailsUseCase<T : Identifiable>(
    private val detailsRepository: DetailsRepository,
    private val mapper: Mapper<T, SpendingDetailEntity>,
) {
    suspend operator fun invoke() =
        detailsRepository.getAllDetails().map { mapper.forUI(it) }
}

