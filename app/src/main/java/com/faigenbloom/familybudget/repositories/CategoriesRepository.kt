package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.db.entities.CategoryEntity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity
import com.faigenbloom.familybudget.datasources.firebase.NetworkDataSource
import com.faigenbloom.familybudget.repositories.mappers.CategorySourceMapper

class CategoriesRepository(
    private val dataSource: BaseDataSource,
    private val networkDataSource: NetworkDataSource,
    private val categorySourceMapper: CategorySourceMapper,
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
        networkDataSource.saveCategory(categorySourceMapper.forServer(categoryEntity))
        dataSource.addCategory(categoryEntity)
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

    suspend fun loadCategories() {
        val loadedCategories = networkDataSource.loadCategories()
        dataSource.saveCategories(loadedCategories.map { categorySourceMapper.forDB(it) })

    }
}
