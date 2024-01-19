package com.faigenbloom.famillyspandings.domain.categories

import android.net.Uri
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.domain.GenerateIdUseCase
import com.faigenbloom.famillyspandings.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetCategoryUseCase(
    private val categoriesRepository: CategoriesRepository,
    private val idGenerator: GenerateIdUseCase,
) {
    suspend operator fun invoke(id: String, name: String, uri: Uri?): String {
        return withContext(Dispatchers.IO) {
            val newCategoryID = idGenerator(id)
            categoriesRepository.addCategory(
                CategoryEntity(
                    id = newCategoryID,
                    isDefault = false,
                    name = name,
                    photoUri = uri?.toString(),
                ),
            )
            newCategoryID
        }
    }
}
