package com.faigenbloom.familybudget.domain.budget

import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.domain.spendings.GetSpentTotalUseCase
import com.faigenbloom.familybudget.repositories.BudgetPageRepository
import com.faigenbloom.familybudget.ui.budget.BudgetLabels
import com.faigenbloom.familybudget.ui.budget.BudgetLineUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBudgetLinesUseCase(
    private val budgetPageRepository: BudgetPageRepository,
    private val generateDefaultBudgetLinesUseCase: GenerateDefaultBudgetLinesUseCase,
    private val getSpentTotalUseCase: GetSpentTotalUseCase,
    private val calculateFormulasUseCase: CalculateFormulasUseCase,
    private val budgetLineMapper: BudgetLineMapper,
) {
    suspend operator fun invoke(
        isForMonth: Boolean,
        isForFamily: Boolean,
        from: Long,
        to: Long,
    ): List<BudgetLineUiData> {
        return withContext(Dispatchers.IO) {
            val budgetLineEntities =
                budgetPageRepository.getBudgetLines(
                    isForMonth, isForFamily,
                    from,
                    to,
                ).let {
                    return@let if (it.isEmpty()) {
                        ArrayList(generateDefaultBudgetLinesUseCase(isForMonth, isForFamily))
                    } else {
                        ArrayList(it)
                    }
                }.recalculateValues(from, to)

            calculateFormulasUseCase(budgetLineEntities)
                .map { budgetLineMapper.forUI(it) }
        }
    }

    private suspend fun ArrayList<BudgetLineEntity>.recalculateValues(
        from: Long,
        to: Long,
    ): ArrayList<BudgetLineEntity> {
        val spent = getSpentTotalUseCase(false, from, to)
        val plannedSpendings = getSpentTotalUseCase(true, from, to)

        setAmount(
            BudgetLabels.PLANNED_SPENDINGS,
            plannedSpendings,
        )
        setAmount(
            BudgetLabels.SPENT,
            spent,
        )
        return this
    }

    private fun ArrayList<BudgetLineEntity>.setAmount(
        key: BudgetLabels,
        value: Long,
    ) {
        val index = indexOfFirst {
            it.repeatableId == key.name
        }
        set(
            index,
            get(index).copy(
                amount = value,
            ),
        )
    }
}


