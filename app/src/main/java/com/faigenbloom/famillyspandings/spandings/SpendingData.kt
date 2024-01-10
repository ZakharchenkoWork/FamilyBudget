package com.faigenbloom.famillyspandings.spandings

import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.Countable
import com.faigenbloom.famillyspandings.comon.toSortableDate
import java.time.LocalDate
import java.util.UUID

data class SpendingData(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: CategoryData,
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
