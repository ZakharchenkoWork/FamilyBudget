package com.faigenbloom.famillyspandings.categories

import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.DefaultCategories

data class CategoryData(
    val id: String,
    val nameId: Int? = null,
    val name: String? = null,
    val iconId: Int? = null,
    val iconUri: String? = null,
) {
    companion object {
        fun fromEntity(categoryEntity: CategoryEntity) =
            if (categoryEntity.isDefault) {
                val defaultCategory = DefaultCategories.valueOf(categoryEntity.id)
                CategoryData(
                    id = defaultCategory.name,
                    nameId = defaultCategory.nameId,
                    iconId = defaultCategory.iconId,
                )
            } else {
                CategoryData(
                    id = categoryEntity.id,
                    name = categoryEntity.name,
                    iconUri = categoryEntity.photoUri,
                )
            }
    }
}
