package com.faigenbloom.familybudget.domain.categories

import android.net.Uri
import com.faigenbloom.familybudget.datasources.entities.CategoryEntity
import com.faigenbloom.familybudget.domain.GenerateIdUseCase
import com.faigenbloom.familybudget.repositories.CategoriesRepository
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
