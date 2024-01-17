package com.faigenbloom.famillyspandings.domain.statistics

import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.domain.categories.GetCategoriesUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetAllSpendingsUseCase
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import com.faigenbloom.famillyspandings.ui.spandings.SpendingUiData
import com.faigenbloom.famillyspandings.ui.statistics.CategorySummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCategorySummariesUseCase(
    private val getCategoriesUseCase: GetCategoriesUseCase<CategoryUiData>,
    private val getAllSpendingsUseCase: GetAllSpendingsUseCase<SpendingUiData>,
) {
    suspend operator fun invoke(): ArrayList<CategorySummary> {
        return withContext(Dispatchers.IO) {
            val allCategories = getCategoriesUseCase()
            val spendings = getAllSpendingsUseCase(false)
            val summaries = ArrayList<CategorySummary>()
            spendings.forEach { spending ->
                summaries.firstOrNull {
                    it.id == spending.categoryId
                }?.let {
                    it.amount += spending.amount.toLongMoney()
                } ?: kotlin.run {
                    val category = allCategories.first {
                        it.id == spending.categoryId
                    }
                    summaries.add(
                        CategorySummary(
                            id = spending.categoryId,
                            nameId = category.nameId,
                            name = category.name,
                            iconId = category.iconId,
                            iconUri = category.iconUri,
                            amount = spending.amount.toLongMoney(),
                        ),
                    )
                }
            }
            summaries
        }
    }
}
