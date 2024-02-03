package com.faigenbloom.familybudget.domain.categories

import com.faigenbloom.familybudget.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteCategoryUseCase(
    private val categoriesRepository: CategoriesRepository,
) {
    suspend operator fun invoke(id: String) {
        return withContext(Dispatchers.IO) {
            val spendingsByCategory = categoriesRepository.getSpendingsByCategory(id)
            if (spendingsByCategory.isEmpty()) {
                categoriesRepository.deleteCategory(id)
            } else {
                categoriesRepository.hideCategory(id)
            }
        }
    }
}
