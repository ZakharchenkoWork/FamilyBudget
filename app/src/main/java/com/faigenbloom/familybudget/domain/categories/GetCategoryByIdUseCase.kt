package com.faigenbloom.familybudget.domain.categories

import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.datasources.entities.CategoryEntity
import com.faigenbloom.familybudget.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCategoryByIdUseCase<T : Identifiable>(
    private val categoriesRepository: CategoriesRepository,
    private val categoriesMapper: Mapper<T, CategoryEntity>,
) {
    suspend operator fun invoke(id: String): T {
        return withContext(Dispatchers.IO) {
            categoriesMapper.forUI(categoriesRepository.getCategoryById(id))
        }
    }
}
