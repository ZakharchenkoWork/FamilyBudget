package com.faigenbloom.famillyspandings.ui.categories

import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.common.Identifiable

data class CategoryUiData(
    override val id: String,
    val nameId: Int? = null,
    val name: String? = null,
    val iconId: Int? = null,
    val iconUri: String? = null,
    val isDefault: Boolean = false,
) : Identifiable {
    fun getResizedIcon(): Int? {
        return iconId?.let {
            //when(id == )
            R.drawable.icon_health_vertical
        }
    }
}
