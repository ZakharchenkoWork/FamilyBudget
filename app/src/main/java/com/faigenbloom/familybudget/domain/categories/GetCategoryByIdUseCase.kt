package com.faigenbloom.familybudget.domain.categories

import com.faigenbloom.familybudget.domain.mappers.CategoriesMapper
import com.faigenbloom.familybudget.repositories.CategoriesRepository
import com.faigenbloom.familybudget.ui.categories.CategoryUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCategoryByIdUseCase(
    private val categoriesRepository: CategoriesRepository,
    private val categoriesMapper: CategoriesMapper,
) {
    suspend operator fun invoke(id: String): CategoryUiData {
        return withContext(Dispatchers.IO) {
            categoriesMapper.forUI(categoriesRepository.getCategoryById(id))
        }
    }
}
