package com.faigenbloom.famillyspandings.datasources.entities

import android.net.Uri
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.Countable
import com.faigenbloom.famillyspandings.comon.toSortableDate
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail
import java.time.LocalDate

data class SpendingEntity(
    val id: String,
    val name: String,
    val amount: Long,
    val date: LocalDate,
    val category: CategoryData,
    val photoUri: Uri?,
    val details: List<SpendingDetail>,
) : Countable {
    override fun getSortableValue(): Long {
        return amount
    }

    override fun getSortableDate(): Int {
        return date.toSortableDate()
    }
}
