package com.faigenbloom.famillyspandings.ui.spandings.list

import com.faigenbloom.famillyspandings.comon.Countable
import com.faigenbloom.famillyspandings.comon.toSortableDate
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import java.time.LocalDate
import java.util.UUID

data class SpendingCategoryUiData(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: CategoryUiData,
    val amount: Long,
    val date: LocalDate,
    val isHidden: Boolean = false,
) : Countable {
    override fun getSortableValue(): Long {
        return amount
    }

    override fun getSortableDate(): Int {
        return date.toSortableDate()
    }
}
