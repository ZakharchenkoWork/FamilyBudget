package com.faigenbloom.familybudget.repositories.mappers

import com.faigenbloom.familybudget.datasources.db.entities.CategoryEntity
import com.faigenbloom.familybudget.datasources.firebase.CategoryModel

class CategorySourceMapper {
    fun forServer(entity: CategoryEntity): CategoryModel {
        return CategoryModel(
            id = entity.id,
            isHidden = entity.isHidden,
            name = entity.name ?: "",
            photoUri = entity.photoUri,
        )
    }

    fun forDB(model: CategoryModel): CategoryEntity {
        return CategoryEntity(
            id = model.id,
            isHidden = model.isHidden,
            name = model.name,
            isDefault = false,
            photoUri = model.photoUri,
        )
    }
}
