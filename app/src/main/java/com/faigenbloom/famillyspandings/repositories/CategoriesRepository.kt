package com.faigenbloom.famillyspandings.repositories

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

class CategoriesRepository(
    private val dataSource: BaseDataSource,
) {
    suspend fun getCategoryById(id: String): CategoryEntity {
        return dataSource.getCategory(id)
    }

    suspend fun getCategories(showHidden: Boolean): List<CategoryEntity> {
        return if (showHidden) {
            dataSource.getAllCategories()
        } else {
            dataSource.getCategoriesByVisibility(false)
        }
    }

    suspend fun addCategory(
        categoryEntity: CategoryEntity,
    ) {
        dataSource.addCategory(
            categoryEntity,
        )
    }

    suspend fun getSpendingsByCategory(id: String): List<SpendingEntity> {
        return dataSource.getSpendingsByCategory(id)
    }

    suspend fun deleteCategory(id: String) {
        dataSource.deleteCategory(id)
    }

    suspend fun hideCategory(id: String) {
        dataSource.changeCategoryHidden(id, true)
    }
}
