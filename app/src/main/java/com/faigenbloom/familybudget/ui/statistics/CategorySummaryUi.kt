package com.faigenbloom.familybudget.ui.statistics

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import coil.compose.rememberImagePainter
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.datasources.db.entities.DefaultCategories

data class CategorySummaryUi(
    val id: String,
    val name: String?,
    val iconUri: String?,
    var barDataValue: Float = 0.0f,
    var amount: Long,
) {
    var amountPercent: Double = 0.0
}

@Composable
fun asColor(categorySummary: CategorySummaryUi): Color {
    return categorySummary.iconUri?.let {
        asColor(iconUri = it.toUri())
    } ?: DefaultCategories.getOrNull(categorySummary.id)?.let {
        asColor(icon = it.iconId)
    } ?: categorySummary.getCategoryNameAsColor()
}

@Composable
private fun CategorySummaryUi.getCategoryNameAsColor(): Color {
    val name = DefaultCategories.getOrNull(id)?.let {
        stringResource(id = it.nameId)
    } ?: name ?: ""

    return Color(name.hashCode() and 0xFFFFFF)
}

@Composable
private fun asColor(icon: Int): Color {
    return Color(
        Palette.from(
            BitmapFactory.decodeResource(
                LocalContext.current.resources,
                icon,
            ),
        ).generate().dominantSwatch?.rgb ?: Color.White.toArgb(),
    )
}

@Composable
private fun asColor(iconUri: Uri): Color {
    return Color(
        Palette.from(
            BitmapFactory.decodeStream(
                LocalContext.current.contentResolver.openInputStream(iconUri),
            ),
        ).generate().dominantSwatch?.rgb ?: Color.White.toArgb(),
    )
}

@Composable
fun CategorySummaryUi.getImage(): Painter {
    return this.iconUri?.let {
        rememberImagePainter(it)
    } ?: DefaultCategories.getOrNull(id)?.let {
        painterResource(id = it.iconId)
    } ?: painterResource(id = R.drawable.icon_photo)
}
