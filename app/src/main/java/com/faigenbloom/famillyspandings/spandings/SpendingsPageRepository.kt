package com.faigenbloom.famillyspandings.spandings

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

class SpendingsPageRepository(private val dataSource: BaseDataSource) {
    suspend fun getAllSpendings(): List<SpendingEntity> {
        return dataSource.getAllSpendings()
    }

    suspend fun getCategoryById(id: String): CategoryEntity {
        return dataSource.getCategory(id)
    }
}
