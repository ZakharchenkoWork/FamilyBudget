package com.faigenbloom.familybudget.domain.budget

import com.faigenbloom.familybudget.ui.budget.Operation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CalculateMoneyUseCase {
    suspend operator fun invoke(firstValue: Long, operation: Operation, secondValue: Long): Long {
        return withContext(Dispatchers.Default) {
            when (operation) {
                Operation.NONE -> firstValue
                Operation.Addition -> firstValue + secondValue
                Operation.Subtraction -> firstValue - secondValue
                Operation.Multiplication -> firstValue * secondValue
                Operation.Division -> firstValue / secondValue// TODO: Probably need to test
            }
        }
    }
}
