package com.faigenbloom.famillyspandings.ui.categories

import com.faigenbloom.famillyspandings.comon.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.DefaultCategories

class CategoriesMapper : Mapper<CategoryUiData, CategoryEntity>() {
    override fun forUI(entity: CategoryEntity): CategoryUiData {
        return if (entity.isDefault) {
            val defaultCategory = DefaultCategories.valueOf(entity.id)
            CategoryUiData(
                id = defaultCategory.name,
                nameId = defaultCategory.nameId,
                iconId = defaultCategory.iconId,
            )
        } else {
            CategoryUiData(
                id = entity.id,
                name = entity.name,
                iconUri = entity.photoUri,
            )
        }
    }

    override fun forDB(model: CategoryUiData): CategoryEntity {
        return CategoryEntity(
            id = model.id,
            isDefault = false,
            name = model.name,
            photoUri = model.iconUri,
        )
    }

    override fun copyChangingId(model: CategoryUiData, id: String): CategoryUiData {
        return model.copy(id = id)
    }
}
