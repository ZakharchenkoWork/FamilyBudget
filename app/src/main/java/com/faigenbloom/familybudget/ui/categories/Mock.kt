package com.faigenbloom.familybudget.ui.categories

import com.faigenbloom.familybudget.datasources.db.entities.DefaultCategories

const val FIRST_CATEGORY = "FIRST_CATEGORY"
const val NEW_CATEGORY_NAME_INPUT = "NEW_CATEGORY_NAME_INPUT"
const val NEW_CATEGORY_SAVE_BUTTON = "NEW_CATEGORY_SAVE_BUTTON"
const val EDIT_CATEGORY_BUTTON = "EDIT_CATEGORY_BUTTON"

val mockCategoriesList = DefaultCategories.values().map {
    CategoryUiData(id = it.name)
}
