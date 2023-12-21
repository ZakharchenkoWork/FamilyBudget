package com.faigenbloom.famillyspandings.categories

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.DefaultCategories
import java.util.UUID

class CategoriesRepository(private val dataSource: BaseDataSource) {
    suspend fun getCategories(): List<CategoryData> {
        return dataSource.getCategories().map {
            if (it.isDefault) {
                val defaultCategory = DefaultCategories.valueOf(it.id)
                CategoryData(
                    id = defaultCategory.name,
                    nameId = defaultCategory.nameId,
                    iconId = defaultCategory.iconId,
                )
            } else {
                CategoryData(
                    id = it.id,
                    name = it.name,
                    iconUri = it.photoUri,
                )
            }
        }
    }

    suspend fun updateCategoryPhoto(id: String, photoUri: String) {
        dataSource.updateCategoryPhoto(
            id = id,
            photoUri = photoUri,
        )
    }

    suspend fun addCategory(newCategoryName: String) {
        dataSource.addCategory(
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                isDefault = false,
                name = newCategoryName,
                photoUri = null,
            ),
        )
    }
}
