package com.faigenbloom.familybudget.repositories.mappers

import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.datasources.firebase.models.BudgetLineModel

class BudgetLineSourceMapper {
    fun forDB(model: BudgetLineModel): BudgetLineEntity {
        return BudgetLineEntity(
            id = model.id,
            repeatableId = model.repeatableId,
            name = model.name,
            amount = model.amount,
            sortableDate = model.sortableDate,
            isForMonth = model.forMonth,
            isForFamily = model.forFamily,
            isDefault = model.default,
            formula = model.formula,
        )
    }

    fun forServer(entity: BudgetLineEntity, ownerId: String): BudgetLineModel {
        return BudgetLineModel(
            id = entity.id,
            repeatableId = entity.repeatableId,
            name = entity.name,
            amount = entity.amount,
            sortableDate = entity.sortableDate,
            forMonth = entity.isForMonth,
            forFamily = entity.isForFamily,
            ownerId = ownerId,
            default = entity.isDefault,
            formula = entity.formula,
        )
    }
}
