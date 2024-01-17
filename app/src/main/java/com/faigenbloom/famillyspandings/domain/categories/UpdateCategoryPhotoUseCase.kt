package com.faigenbloom.famillyspandings.domain.categories

import com.faigenbloom.famillyspandings.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateCategoryPhotoUseCase(
    private val categoriesRepository: CategoriesRepository,
) {
    suspend operator fun invoke(id: String, photoUri: String) {
        withContext(Dispatchers.IO) {
            categoriesRepository.updateCategoryPhoto(id, photoUri)
        }
    }
}
