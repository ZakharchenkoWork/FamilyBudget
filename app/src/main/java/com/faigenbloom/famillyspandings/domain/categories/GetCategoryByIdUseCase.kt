package com.faigenbloom.famillyspandings.domain.categories

import com.faigenbloom.famillyspandings.common.Identifiable
import com.faigenbloom.famillyspandings.common.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.repositories.CategoriesRepository
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
