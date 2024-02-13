package com.faigenbloom.familybudget.domain.budget

import com.faigenbloom.familybudget.common.getCurrentDate
import com.faigenbloom.familybudget.common.toJson
import com.faigenbloom.familybudget.common.toSortableDate
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.domain.GenerateIdUseCase
import com.faigenbloom.familybudget.ui.budget.BudgetLabels
import com.faigenbloom.familybudget.ui.budget.Operation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenerateDefaultBudgetLinesUseCase(
    private val idGenerator: GenerateIdUseCase,
) {
    suspend operator fun invoke(
        isForMonth: Boolean,
        isForFamily: Boolean,
    ): List<BudgetLineEntity> {
        return withContext(Dispatchers.Default) {
            generateEnities(isForMonth, isForFamily)
        }
    }

    private fun generateEnities(
        isForMonth: Boolean,
        isForFamily: Boolean,
    ): ArrayList<BudgetLineEntity> {
        return ArrayList<BudgetLineEntity>().apply {
            BudgetLabels.values().forEach {
                add(
                    createDefaultEntity(
                        it.name,
                        isForMonth,
                        isForFamily,
                    ),
                )
            }
        }
    }

    private fun createDefaultEntity(
        id: String,
        isForMonth: Boolean,
        isForFamily: Boolean,
    ): BudgetLineEntity {
        return BudgetLineEntity(
            id = idGenerator(),
            repeatableId = id,
            name = "",
            amount = 0L,
            sortableDate = getCurrentDate().toSortableDate().toLong(),
            isForMonth = isForMonth,
            isForFamily = isForFamily,
            isDefault = true,
            formula = if (id == BudgetLabels.BALANCE.name) {
                FormulaEntity(
                    actions = listOf(
                        Operation.Subtraction,
                        Operation.Subtraction,
                    ),
                    budgetLineIds = listOf(
                        BudgetLabels.PLANNED_BUDGET.name,
                        BudgetLabels.SPENT.name,
                        BudgetLabels.PLANNED_SPENDINGS.name,
                    ),
                ).toJson()
            } else null,
        )
    }
}
