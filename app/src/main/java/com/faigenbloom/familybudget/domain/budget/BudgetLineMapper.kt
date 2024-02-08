package com.faigenbloom.familybudget.domain.budget

import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.ui.budget.BudgetLineUiData

class BudgetLineMapper {
    fun forUI(entity: BudgetLineEntity): BudgetLineUiData {
        return BudgetLineUiData(
            id = entity.id,
            name = entity.name,
            amount = entity.amount.toReadableMoney(),
        )
    }

    /*  fun forDB(model: BudgetLineUiData): BudgetLineEntity {
          return BudgetLineEntity(
              id = model.id,
              name = model.name,
              amount = model.amount.toLongMoney(),
          )
      }*/
}