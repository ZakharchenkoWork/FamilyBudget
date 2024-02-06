package com.faigenbloom.familybudget.ui.categories

import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.datasources.db.entities.CategoryEntity
import com.faigenbloom.familybudget.datasources.db.entities.DefaultCategories

class CategoriesMapper : Mapper<CategoryUiData, CategoryEntity>() {
    override fun forUI(entity: CategoryEntity): CategoryUiData {
        return if (entity.isDefault) {
            val defaultCategory = DefaultCategories.valueOf(entity.id)
            CategoryUiData(
                id = defaultCategory.name,
                nameId = defaultCategory.nameId,
                iconId = defaultCategory.iconId,
                isDefault = true,
            )
        } else {
            CategoryUiData(
                id = entity.id,
                name = entity.name,
                iconUri = entity.photoUri,
                isDefault = false,
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
