package com.faigenbloom.familybudget.ui.categories

import com.faigenbloom.familybudget.common.Identifiable

data class CategoryUiData(
    override val id: String,
    val nameId: Int? = null,
    val name: String? = null,
    val iconId: Int? = null,
    val iconUri: String? = null,
    val isDefault: Boolean = false,
) : Identifiable
