package com.faigenbloom.famillyspandings.categories

import com.faigenbloom.famillyspandings.datasources.BaseDataSource

class CategoriesRepository(private val dataSource: BaseDataSource) {
    suspend fun getCategories(): List<CategoryData> {
        return dataSource.getCategories()
    }
}
