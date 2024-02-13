package com.faigenbloom.familybudget.ui.categories

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.rememberImagePainter
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.datasources.db.entities.DefaultCategories

data class CategoryUiData(
    override val id: String,
    val name: String? = null,
    val icon: String? = null,
    val isDefault: Boolean = false,
) : Identifiable

@Composable
fun CategoryUiData.getCategoryIcon(isSmall: Boolean = true, isCircle: Boolean = false): Painter {
    return icon?.let {
        if (isCircle) {
            rememberImagePainter(it) {
                transformations()
            }
        } else {
            rememberImagePainter(it)
        }
    } ?: DefaultCategories.getOrNull(id)?.let {
        painterResource(
            id = if (isSmall) it.iconId else it.iconBigId,
        )
    } ?: painterResource(R.drawable.icon)
}

@Composable
fun CategoryUiData.getCategoryName(): String {
    return DefaultCategories.getOrNull(id)?.let {
        stringResource(id = it.nameId)
    } ?: name ?: ""
}
