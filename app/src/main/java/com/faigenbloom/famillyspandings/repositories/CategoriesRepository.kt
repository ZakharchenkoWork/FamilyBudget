package com.faigenbloom.famillyspandings.repositories

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity

class CategoriesRepository(
    private val dataSource: BaseDataSource,
) {
    suspend fun getCategoryById(id: String): CategoryEntity {
        return dataSource.getCategory(id)
    }

    suspend fun getCategories(): List<CategoryEntity> {
        return dataSource.getAllCategories()
    }

    suspend fun updateCategoryPhoto(id: String, photoUri: String) {
        dataSource.updateCategoryPhoto(
            id = id,
            photoUri = photoUri,
        )
    }

    suspend fun addCategory(
        categoryEntity: CategoryEntity,
    ) {
        dataSource.addCategory(
            categoryEntity,
        )
    }
}
