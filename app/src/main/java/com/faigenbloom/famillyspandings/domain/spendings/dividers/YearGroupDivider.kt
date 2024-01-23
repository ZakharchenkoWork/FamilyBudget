package com.faigenbloom.famillyspandings.domain.spendings.dividers

import com.faigenbloom.famillyspandings.common.toSortableDate
import com.faigenbloom.famillyspandings.domain.spendings.Divider
import com.faigenbloom.famillyspandings.ui.spendings.list.SpendingCategoryUiData

class YearGroupDivider(
    startDate: Long,
    endDate: Long,
) : Divider<SpendingCategoryUiData>(startDate, endDate) {
    override fun prepareGroups(
        list: List<SpendingCategoryUiData>,
    ): List<List<SpendingCategoryUiData>> {
        return list.filter {
            startDate.toSortableDate() <= it.getSortableDate() &&
                    it.getSortableDate() <= endDate.toSortableDate()
        }.groupBy {
            it.getSortableDate() / 10000
        }.map {
            it.value
        }.sortedByDescending {
            it[0].getSortableDate()
        }
    }
}
