package com.faigenbloom.famillyspandings.domain.spendings

import com.faigenbloom.famillyspandings.common.getCurrentDate
import com.faigenbloom.famillyspandings.common.toLocalDate
import com.faigenbloom.famillyspandings.ui.spendings.list.SpendingCategoryUiData
import java.time.LocalDate

data class DatedList(val patterns: List<Pattern<SpendingCategoryUiData>>) {
    val date: LocalDate =
        patterns.firstOrNull()?.items?.firstOrNull()?.date ?: getCurrentDate().toLocalDate()

    operator fun get(index: Int): Pattern<SpendingCategoryUiData> {
        return patterns[index]
    }
}
