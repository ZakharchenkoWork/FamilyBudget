package com.faigenbloom.famillyspandings.domain.spendings.dividers

import com.faigenbloom.famillyspandings.domain.spendings.Divider
import com.faigenbloom.famillyspandings.ui.spandings.list.SpendingCategoryUiData

class YearGroupDivider : Divider<SpendingCategoryUiData> {
    override fun prepareGroups(data: List<SpendingCategoryUiData>): List<List<SpendingCategoryUiData>> {
        return data.groupBy {
            it.getSortableDate() / 10000
        }.map {
            it.value
        }.sortedByDescending {
            it[0].getSortableDate()
        }
    }
}
