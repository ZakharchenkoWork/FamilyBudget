package com.faigenbloom.famillyspandings.domain.categories

import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.domain.GenerateIdUseCase
import com.faigenbloom.famillyspandings.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddCategoryUseCase(
    private val categoriesRepository: CategoriesRepository,
    private val idGenerator: GenerateIdUseCase,
) {
    suspend operator fun invoke(newCategoryName: String): String {
        return withContext(Dispatchers.IO) {
            val newCategoryID = idGenerator()
            categoriesRepository.addCategory(
                CategoryEntity(
                    id = newCategoryID,
                    isDefault = false,
                    name = newCategoryName,
                ),
            )
            newCategoryID
        }
    }
}
