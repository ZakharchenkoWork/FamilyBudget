package com.faigenbloom.familybudget.datasources.db.entities

import com.faigenbloom.familybudget.common.getCurrentDate
import com.faigenbloom.familybudget.common.toSortableDate
import com.faigenbloom.familybudget.ui.budget.BudgetLabels

val defaultBudgetEntities = ArrayList<BudgetLineEntity>().apply {
    BudgetLabels.values().forEach {
        addAll(createEntitiesForId(it.name))
    }
}

private fun createDefaultEntity(
    id: String,
    isForMonth: Boolean,
    isForFamily: Boolean,
): BudgetLineEntity {
    return BudgetLineEntity(
        id = id,
        name = "",
        amount = 0L,
        sortableDate = getCurrentDate().toSortableDate().toLong(),
        isForMonth = isForMonth,
        isForFamily = isForFamily,
        isDefault = true,
    )
}

private fun createEntitiesForId(
    id: String,
): List<BudgetLineEntity> {
    return listOf(
        createDefaultEntity(
            id = id,
            isForMonth = true,
            isForFamily = false,
        ),
        createDefaultEntity(
            id = id,
            isForMonth = false,
            isForFamily = false,
        ),
        createDefaultEntity(
            id = id,
            isForMonth = false,
            isForFamily = true,
        ),
        createDefaultEntity(
            id = id,
            isForMonth = true,
            isForFamily = true,
        ),
    )
}
