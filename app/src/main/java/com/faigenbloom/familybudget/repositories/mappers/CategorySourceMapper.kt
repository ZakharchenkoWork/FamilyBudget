package com.faigenbloom.familybudget.repositories.mappers

import com.faigenbloom.familybudget.datasources.db.entities.CategoryEntity
import com.faigenbloom.familybudget.datasources.firebase.models.CategoryModel

class CategorySourceMapper {
    fun forServer(entity: CategoryEntity): CategoryModel {
        return CategoryModel(
            id = entity.id,
            hidden = entity.isHidden,
            name = entity.name ?: "",
            photoUri = entity.photoUri,
        )
    }

    fun forDB(model: CategoryModel): CategoryEntity {
        return CategoryEntity(
            id = model.id,
            isHidden = model.hidden,
            name = model.name,
            isDefault = false,
            photoUri = model.photoUri,
        )
    }
}
