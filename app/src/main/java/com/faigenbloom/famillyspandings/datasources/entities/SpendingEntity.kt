package com.faigenbloom.famillyspandings.datasources.entities

import android.net.Uri
import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.edit.SpendingDetail

data class SpendingEntity(
    val id: String,
    val name: String,
    val amount: Long,
    val date: Long,
    val category: CategoryData,
    val photoUri: Uri?,
    val details: List<SpendingDetail>,
)
