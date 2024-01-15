package com.faigenbloom.famillyspandings.categories

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.id.IdGenerator

class CategoriesRepository(
    private val dataSource: BaseDataSource,
    private val idGenerator: IdGenerator,
) {
    suspend fun getCategories(): List<CategoryData> {
        return dataSource.getAllCategories().map {
            CategoryData.fromEntity(it)
        }
    }

    suspend fun updateCategoryPhoto(id: String, photoUri: String) {
        dataSource.updateCategoryPhoto(
            id = id,
            photoUri = photoUri,
        )
    }

    suspend fun addCategory(newCategoryName: String): String {
        val id = idGenerator.checkOrGenId()
        dataSource.addCategory(
            CategoryEntity(
                id = id,
                isDefault = false,
                name = newCategoryName,
                photoUri = null,
            ),
        )
        return id
    }
}
