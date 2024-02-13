package com.faigenbloom.familybudget.domain.budget

import com.faigenbloom.familybudget.ui.budget.BudgetLineUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReCalculateFormulasUseCase(
    private val calculateFormulasUseCase: CalculateFormulasUseCase,
    private val budgetLineMapper: BudgetLineMapper,
) {
    suspend operator fun invoke(
        budgetLineUiData: List<BudgetLineUiData>,
    ): List<BudgetLineUiData> {
        return withContext(Dispatchers.Default) {
            calculateFormulasUseCase(
                budgetLineUiData.map {
                    budgetLineMapper.forDB(
                        model = it,
                        date = 0L,// We don't care about those values here
                        isForMonth = false,//they will be lost after mapping any way
                        isForFamily = false,// so we can use anything
                    )
                },
            ).map {
                budgetLineMapper.forUI(it)
            }
        }
    }
}
