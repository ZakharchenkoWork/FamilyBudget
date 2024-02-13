package com.faigenbloom.familybudget.ui.spendings.list

import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.common.toSortableDate
import com.faigenbloom.familybudget.domain.spendings.Countable
import com.faigenbloom.familybudget.ui.categories.CategoryUiData
import java.time.LocalDate
import java.util.UUID

data class SpendingCategoryUiData(
    override val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: CategoryUiData,
    val amount: Long,
    val date: LocalDate,
    val photoUri: String? = null,
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
