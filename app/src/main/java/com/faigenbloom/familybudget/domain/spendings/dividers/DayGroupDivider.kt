package com.faigenbloom.familybudget.domain.spendings.dividers

import com.faigenbloom.familybudget.common.toSortableDate
import com.faigenbloom.familybudget.domain.spendings.Divider
import com.faigenbloom.familybudget.ui.spendings.list.SpendingCategoryUiData

class DayGroupDivider(
    startDate: Long,
    endDate: Long,
) : Divider<SpendingCategoryUiData>(startDate, endDate) {
    override fun prepareGroups(
        list: List<SpendingCategoryUiData>,
    ): List<List<SpendingCategoryUiData>> {
        return list.filter {
            startDate.toSortableDate() <= it.getSortableDate() &&
                    it.getSortableDate() <= endDate.toSortableDate()
        }.groupBy { it.getSortableDate() }.map {
            it.value
        }.sortedByDescending {
            it[0].getSortableDate()
        }
    }
}
