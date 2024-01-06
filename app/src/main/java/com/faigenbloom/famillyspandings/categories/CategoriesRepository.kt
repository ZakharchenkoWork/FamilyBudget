package com.faigenbloom.famillyspandings.categories

import com.faigenbloom.famillyspandings.comon.checkOrGenId
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

    suspend fun addCategory(newCategoryName: String): String {
        val id = "".checkOrGenId()
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
