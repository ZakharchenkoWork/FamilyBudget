package com.faigenbloom.famillyspandings.categories

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.DefaultCategories

class CategoriesRepository(private val dataSource: BaseDataSource) {
    suspend fun getCategories(): List<CategoryData> {
        return dataSource.getCategories().map {
            val defaultCategory = DefaultCategories.valueOf(it.id)
            CategoryData(
                id = defaultCategory.name,
                nameId = defaultCategory.nameId,
                iconId = defaultCategory.iconId,
            )
        }
    }
}
