package com.faigenbloom.familybudget.domain.budget

import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.domain.spendings.GetSpentTotalUseCase
import com.faigenbloom.familybudget.repositories.BudgetPageRepository
import com.faigenbloom.familybudget.ui.budget.BudgetLabels
import com.faigenbloom.familybudget.ui.budget.BudgetLineUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBudgetLinesUseCase(
    private val budgetPageRepository: BudgetPageRepository,
    private val getSpentTotalUseCase: GetSpentTotalUseCase,
    private val getBudgetUseCase: GetBudgetUseCase,
    private val budgetLineMapper: BudgetLineMapper,
) {
    suspend operator fun invoke(from: Long, to: Long): List<BudgetLineUiData> {
        return withContext(Dispatchers.IO) {
            val budgetLinesForUi = ArrayList<BudgetLineUiData>().apply {
                val budgetLinesFromDB = budgetPageRepository.getBudgetLines()
                val spent = getSpentTotalUseCase(false, from, to)
                val plannedSpendings = getSpentTotalUseCase(true, from, to)
                val plannedBudget =
                    budgetLinesFromDB.first { it.id.contains(BudgetLabels.PLANNED_BUDGET.name) }

                add(
                    budgetLineMapper.forUI(plannedBudget),
                )
                add(
                    BudgetLineUiData(
                        id = BudgetLabels.SPENT.name,
                        name = "",
                        amount = spent.toReadableMoney(),
                    ),
                )
                add(
                    BudgetLineUiData(
                        id = BudgetLabels.PLANNED_SPENDINGS.name,
                        name = "",
                        amount = plannedSpendings.toReadableMoney(),
                    ),
                )
                add(
                    BudgetLineUiData(
                        id = BudgetLabels.BALANCE.name,
                        name = "",
                        amount = (plannedBudget.amount - spent - plannedSpendings).toReadableMoney(),
                    ),
                )
                addAll(
                    budgetLinesFromDB
                        .filter { it.id.contains(BudgetLabels.PLANNED_BUDGET.name).not() }
                        .map { budgetLineMapper.forUI(it) },
                )
            }

            budgetLinesForUi
        }
    }
}


