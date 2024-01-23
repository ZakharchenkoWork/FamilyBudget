package com.faigenbloom.famillyspandings.domain.categories

import com.faigenbloom.famillyspandings.common.Identifiable
import com.faigenbloom.famillyspandings.common.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCategoriesUseCase<T : Identifiable>(
    private val categoriesRepository: CategoriesRepository,
    private val categoriesMapper: Mapper<T, CategoryEntity>,
) {
    suspend operator fun invoke(showHidden: Boolean): List<T> {
        return withContext(Dispatchers.IO) {
            categoriesRepository.getCategories(showHidden)
                .sortedBy {
                    it.isDefault
                }.map {
                    categoriesMapper.forUI(it)
                }
        }
    }
}
