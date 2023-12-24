package com.faigenbloom.famillyspandings.categories

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity

class CategoriesRepository(private val dataSource: BaseDataSource) {
    suspend fun getCategories(): List<CategoryData> {
        return dataSource.getCategories().map {
            CategoryData.fromEntity(it)
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
                id = "",
                isDefault = false,
                name = newCategoryName,
                photoUri = null,
            ),
        )
    }
}
