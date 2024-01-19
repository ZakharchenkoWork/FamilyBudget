package com.faigenbloom.famillyspandings.domain.spendings.dividers

import com.faigenbloom.famillyspandings.domain.spendings.Divider
import com.faigenbloom.famillyspandings.ui.spandings.list.SpendingCategoryUiData

class DayGroupDivider : Divider<SpendingCategoryUiData> {
    override fun prepareGroups(data: List<SpendingCategoryUiData>): List<List<SpendingCategoryUiData>> {
        return data.groupBy { it.getSortableDate() }.map {
            it.value
        }.sortedByDescending {
            it[0].getSortableDate()
        }
    }
}
