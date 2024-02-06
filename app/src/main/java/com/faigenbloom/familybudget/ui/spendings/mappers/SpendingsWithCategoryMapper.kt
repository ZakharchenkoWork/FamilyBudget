package com.faigenbloom.familybudget.ui.spendings.mappers

import com.faigenbloom.familybudget.common.toLocalDate
import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity
import com.faigenbloom.familybudget.ui.categories.CategoryUiData
import com.faigenbloom.familybudget.ui.spendings.list.SpendingCategoryUiData

class SpendingsWithCategoryMapper {
    fun forUI(entity: SpendingEntity, category: CategoryUiData): SpendingCategoryUiData {
        return SpendingCategoryUiData(
            id = entity.id,
            name = entity.name,
            category = category,
            amount = entity.amount,
            date = entity.date.toLocalDate(),
            isHidden = entity.isHidden,
            isPlanned = entity.isPlanned,
        )
    }
}
