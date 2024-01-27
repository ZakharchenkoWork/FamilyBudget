package com.faigenbloom.famillyspandings.ui.spendings.list

import com.faigenbloom.famillyspandings.common.Identifiable
import com.faigenbloom.famillyspandings.common.toSortableDate
import com.faigenbloom.famillyspandings.domain.spendings.Countable
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import java.time.LocalDate
import java.util.UUID

data class SpendingCategoryUiData(
    override val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: CategoryUiData,
    val amount: Long,
    val date: LocalDate,
    val isHidden: Boolean = false,
    val isPlanned: Boolean = false,
) : Identifiable, Countable {
    override fun getSortableValue(): Long {
        return amount
    }

    override fun getSortableDate(): Int {
        return date.toSortableDate()
    }
}
