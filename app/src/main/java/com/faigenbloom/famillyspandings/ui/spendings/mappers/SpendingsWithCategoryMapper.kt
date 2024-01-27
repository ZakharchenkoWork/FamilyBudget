package com.faigenbloom.famillyspandings.ui.spendings.mappers

import com.faigenbloom.famillyspandings.common.toLocalDate
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import com.faigenbloom.famillyspandings.ui.spendings.list.SpendingCategoryUiData

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
