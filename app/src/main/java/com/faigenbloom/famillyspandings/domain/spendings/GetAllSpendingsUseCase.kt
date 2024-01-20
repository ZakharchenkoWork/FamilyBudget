package com.faigenbloom.famillyspandings.domain.spendings

import com.faigenbloom.famillyspandings.comon.Identifiable
import com.faigenbloom.famillyspandings.comon.Mapper
import com.faigenbloom.famillyspandings.comon.ONE_MONTH
import com.faigenbloom.famillyspandings.comon.ONE_YEAR
import com.faigenbloom.famillyspandings.comon.getCurrentDate
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.repositories.SpendingsRepository

class GetAllSpendingsUseCase<T : Identifiable>(
    private val spendingsRepository: SpendingsRepository,
    private val mapper: Mapper<T, SpendingEntity>,
) {

    suspend operator fun invoke(isPlanned: Boolean): List<T> {
        return spendingsRepository.getSpendings(isPlanned)
            .map { mapper.forUI(it) }
    }
}

sealed class FilterType(val from: Long, val to: Long) {
    class Daily(from: Long = getCurrentDate() - ONE_MONTH, to: Long = getCurrentDate()) :
        FilterType(from, to) {
        constructor(isPlanned: Boolean) : this(
            from = getCurrentDate() - ONE_MONTH,
            to = if (isPlanned) {
                getCurrentDate() + ONE_MONTH
            } else {
                getCurrentDate()
            },
        )
    }

    class Monthly(from: Long = getCurrentDate() - ONE_YEAR, to: Long = getCurrentDate()) :
        FilterType(from, to) {
        constructor(isPlanned: Boolean) : this(
            from = getCurrentDate() - ONE_YEAR,
            to = if (isPlanned) {
                getCurrentDate() + ONE_YEAR
            } else {
                getCurrentDate()
            },
        )
    }

    class Yearly(from: Long = getCurrentDate() - 5 * ONE_YEAR, to: Long = getCurrentDate()) :
        FilterType(from, to) {

        constructor(isPlanned: Boolean) : this(
            from = getCurrentDate() - 5 * ONE_YEAR,
            to = if (isPlanned) {
                getCurrentDate() + 5 * ONE_YEAR
            } else {
                getCurrentDate()
            },
        )
    }
}
