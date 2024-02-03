package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.common.getCurrentDate
import com.faigenbloom.familybudget.common.toLocalDate
import com.faigenbloom.familybudget.ui.spendings.list.SpendingCategoryUiData
import java.time.LocalDate

data class DatedList(val patterns: List<Pattern<SpendingCategoryUiData>>) {
    val date: LocalDate =
        patterns.firstOrNull()?.items?.firstOrNull()?.date ?: getCurrentDate().toLocalDate()

    operator fun get(index: Int): Pattern<SpendingCategoryUiData> {
        return patterns[index]
    }
}
