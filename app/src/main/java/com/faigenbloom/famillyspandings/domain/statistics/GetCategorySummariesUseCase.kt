package com.faigenbloom.famillyspandings.domain.statistics

import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.domain.categories.GetCategoriesUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetAllSpendingsUseCase
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import com.faigenbloom.famillyspandings.ui.spandings.SpendingUiData
import com.faigenbloom.famillyspandings.ui.statistics.CategorySummaryUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCategorySummariesUseCase(
    private val getCategoriesUseCase: GetCategoriesUseCase<CategoryUiData>,
    private val getAllSpendingsUseCase: GetAllSpendingsUseCase<SpendingUiData>,
) {
    suspend operator fun invoke(): ArrayList<CategorySummaryUi> {
        return withContext(Dispatchers.IO) {
            val allCategories = getCategoriesUseCase(true)
            val spendings = getAllSpendingsUseCase(false)
            var summaries = ArrayList<CategorySummaryUi>()
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
                        CategorySummaryUi(
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
            val sum = summaries.sumOf { it.amount }
            val max = summaries.maxOf { it.amount }
            summaries.forEach {
                it.amountPercent = (it.amount.toDouble() / sum.toDouble()) * 100.0
            }
            summaries.forEach {
                it.barDataValue = it.amount.toFloat() / max.toFloat()
            }
            summaries = ArrayList(
                summaries.filter {
                    it.amountPercent > 0
                },
            )
            summaries
        }
    }
}
