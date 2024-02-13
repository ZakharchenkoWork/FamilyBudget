package com.faigenbloom.familybudget.domain.budget

import com.faigenbloom.familybudget.common.NO_ANSWER
import com.faigenbloom.familybudget.common.fromJson
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.ui.budget.Operation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CalculateFormulasUseCase {
    private var currentFormulas = HashMap<String, Long>()

    /**
     * Note: amount can be NO_ANSWER(Long.MIN_VALUE) if the formula depends on itself(circular)
     */
    suspend operator fun invoke(budgetLineEntities: List<BudgetLineEntity>): List<BudgetLineEntity> {
        return withContext(Dispatchers.Default) {
            currentFormulas = HashMap()
            val budgetLines = ArrayList(budgetLineEntities)
            budgetLines.forEachIndexed { index, budgetLineEntity ->
                budgetLineEntity.formula?.let {
                    val amount = calculateFormula(budgetLineEntities, it)

                    budgetLines[index] = budgetLineEntity.copy(amount = amount)
                }
            }
            budgetLines
        }
    }

    private fun calculateFormula(
        budgetLineEntities: List<BudgetLineEntity>,
        formulaString: String,
    ): Long {
        val previousAnswer = currentFormulas[formulaString]

        if (previousAnswer != null && previousAnswer != NO_ANSWER) {
            return previousAnswer
        } else if (previousAnswer == NO_ANSWER) {
            return NO_ANSWER
        }
        currentFormulas[formulaString] = NO_ANSWER
        val formulaEntity: FormulaEntity = formulaString.fromJson()
        val requiredLines =
            formulaEntity.budgetLineIds.map { id -> budgetLineEntities.first { it.repeatableId == id } }

        var result = 0L
        requiredLines.forEachIndexed { index, item ->
            val amount = item.formula?.let {
                calculateFormula(budgetLineEntities, it)
            } ?: item.amount
            if (amount == NO_ANSWER) {
                return NO_ANSWER
            }
            result = if (index == 0) {
                amount
            } else {
                when (formulaEntity.actions[index - 1]) {
                    Operation.Addition -> result + amount
                    Operation.NONE -> result
                    Operation.Subtraction -> result - amount
                    Operation.Multiplication -> result * amount
                    Operation.Division -> result / amount
                }
            }
        }
        currentFormulas[formulaString] = result
        return result
    }
}

data class FormulaEntity(
    val actions: List<Operation>,
    val budgetLineIds: List<String>,
)
