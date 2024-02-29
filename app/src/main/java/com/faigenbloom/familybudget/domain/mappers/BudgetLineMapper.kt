package com.faigenbloom.familybudget.domain.mappers

import com.faigenbloom.familybudget.common.toLongMoney
import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.ui.budget.BudgetLineUiData

class BudgetLineMapper {
    fun forUI(entity: BudgetLineEntity): BudgetLineUiData {
        return BudgetLineUiData(
            id = entity.id,
            repeatableId = entity.repeatableId,
            name = entity.name,
            amount = entity.amount.toReadableMoney(),
            isDefault = entity.isDefault,
            formula = entity.formula,
        )
    }

    fun forDB(
        model: BudgetLineUiData,
        date: Long,
        isForMonth: Boolean,
        isForFamily: Boolean,
    ): BudgetLineEntity {
        return BudgetLineEntity(
            id = model.id,
            repeatableId = model.repeatableId,
            name = model.name,
            amount = model.amount.toLongMoney(),
            sortableDate = date,
            isForMonth = isForMonth,
            isForFamily = isForFamily,
            isDefault = model.isDefault,
            formula = model.formula,
        )
    }
}
